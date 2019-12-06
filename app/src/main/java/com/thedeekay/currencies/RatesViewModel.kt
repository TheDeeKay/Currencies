package com.thedeekay.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.currencies.Event.*
import com.thedeekay.domain.Currency
import com.thedeekay.domain.EUR
import com.thedeekay.domain.Money
import com.thedeekay.domain.times
import com.thedeekay.exchangerates.CalculateRatesUseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

/**
 * View model for the currencies conversion screen.
 */
class RatesViewModel(
    private val calculateRatesUseCase: CalculateRatesUseCase
) : ViewModel() {

    private val stateMachine = StateMachine()

    private val mainCurrencySubject = BehaviorSubject.createDefault(0L * EUR)

    val currencyAmounts: Flowable<List<CurrencyUiModel>> by lazy {
        stateMachine.stateFlowable
            .map { state ->
                val mainCurrency = state.mainCurrency
                listOf(
                    MainCurrency(
                        mainCurrency.currency,
                        if (mainCurrency.amount.compareTo(BigDecimal.ZERO) == 0) ""
                        else mainCurrency.amount.toString()
                    )
                ) + state.convertedCurrencies.map {
                    ConvertedCurrency(it)
                }
            }
            .mergeWith(
                mainCurrencySubject
                    .observeOn(Schedulers.io())
                    .toFlowable(BackpressureStrategy.LATEST)
                    .switchMap { mainCurrency ->
                        calculateRatesUseCase.execute(mainCurrency)
                            .map { Pair(it, mainCurrency) }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        stateMachine.handleEvent(
                            NewConvertedCurrencies(
                                it.first,
                                it.second.currency
                            )
                        )
                    }
                    .ignoreElements()
            )
            .distinctUntilChanged()
    }

    fun setMainCurrency(amount: String, currencyCode: String) {
        stateMachine.handleEvent(NewMainCurrency(amount, Currency(currencyCode)))
        mainCurrencySubject.onNext(Money(amount.stringToBigDecimal(), Currency(currencyCode)))
    }

    fun setNewMainCurrencyAmount(amount: String) {
        stateMachine.handleEvent(MainCurrencyNewAmount(amount))
        val bigDecimalAmount = amount.stringToBigDecimal()
        mainCurrencySubject.onNext(mainCurrencySubject.value!!.copy(amount = bigDecimalAmount))
    }
}

private class StateMachine {

    private var state: State = State(Money(BigDecimal.ZERO, EUR), emptyList())
    private val subject = BehaviorSubject.createDefault(state)

    val stateFlowable: Flowable<State> = subject.toFlowable(BackpressureStrategy.LATEST)

    fun handleEvent(event: Event) {
        state = when (event) {

            is MainCurrencyNewAmount -> {
                val bigDecimalAmount = event.amount.stringToBigDecimal()
                state.copy(mainCurrency = state.mainCurrency.copy(amount = bigDecimalAmount))
            }

            is NewMainCurrency -> {
                if (event.currency == state.mainCurrency.currency) {
                    state.copy(
                        mainCurrency = state.mainCurrency.copy(
                            amount = event.amount.stringToBigDecimal()
                        )
                    )
                } else {
                    val newMainCurrency = Money(event.amount.stringToBigDecimal(), event.currency)
                    val oldMainCurrency = state.mainCurrency
                    val newConverted =
                        state.convertedCurrencies.filter { it.currency != newMainCurrency.currency } + listOf(
                            oldMainCurrency
                        ).sortedBy { it.currency.currencyCode }

                    State(newMainCurrency, newConverted)
                }
            }

            is NewConvertedCurrencies -> {
                if (event.baseCurrency != state.mainCurrency.currency) state
                else state.copy(convertedCurrencies = event.convertedCurrencies)
            }
        }
        subject.onNext(state)
    }
}

private data class State(
    val mainCurrency: Money,
    val convertedCurrencies: List<Money>
)

private sealed class Event {

    data class MainCurrencyNewAmount(val amount: String) : Event()

    data class NewMainCurrency(val amount: String, val currency: Currency) : Event()

    data class NewConvertedCurrencies(
        val convertedCurrencies: List<Money>,
        val baseCurrency: Currency
    ) : Event()
}

private fun String.stringToBigDecimal(): BigDecimal {
    return if (isNotEmpty()) {
        val bigDecimal = BigDecimal(this)
        if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) BigDecimal.ZERO
        else bigDecimal
    } else BigDecimal.ZERO
}


class RatesViewModelFactory @Inject constructor(
    private val calculateRatesUseCase: Provider<CalculateRatesUseCase>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RatesViewModel(calculateRatesUseCase.get()) as T
    }
}
