package com.thedeekay.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.Currency
import com.thedeekay.domain.EUR
import com.thedeekay.domain.Money
import com.thedeekay.domain.times
import com.thedeekay.exchangerates.CalculateRatesUseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Provider

/**
 * View model for the currencies conversion screen.
 */
class RatesViewModel(
    private val calculateRatesUseCase: CalculateRatesUseCase
) : ViewModel() {

    private val mainCurrencySubject = BehaviorSubject.createDefault(0L * EUR)

    val currencyAmounts: Flowable<List<CurrencyUiModel>> =
        mainCurrencySubject
            .toFlowable(BackpressureStrategy.LATEST)
            .switchMap { mainCurrency ->
                calculateRatesUseCase.execute(mainCurrency)
                    .map { currencies ->
                        listOf(
                            MainCurrency(mainCurrency.currency, mainCurrency.amount.toString())
                        ) + currencies.map { ConvertedCurrency(it) }
                    }
            }

    fun setMainCurrency(amount: String, currencyCode: String) {
        mainCurrencySubject.onNext(Money(amount, Currency(currencyCode)))
    }

    fun setNewMainCurrencyAmount(amount: String) {
        val bigDecimalAmount = if (amount.isNotEmpty()) BigDecimal(amount) else BigDecimal.ZERO
        mainCurrencySubject.onNext(mainCurrencySubject.value!!.copy(amount = bigDecimalAmount))
    }
}

class RatesViewModelFactory @Inject constructor(
    private val calculateRatesUseCase: Provider<CalculateRatesUseCase>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RatesViewModel(calculateRatesUseCase.get()) as T
    }
}
