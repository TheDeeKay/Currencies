package com.thedeekay.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.Currency
import com.thedeekay.domain.EUR
import com.thedeekay.domain.Money
import com.thedeekay.exchangerates.CalculateRatesUseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Provider

/**
 * View model for the currencies conversion screen.
 */
class RatesViewModel(
    private val calculateRatesUseCase: CalculateRatesUseCase
) : ViewModel() {

    private val mainCurrencySubject = BehaviorSubject.createDefault(Pair("0", EUR.currencyCode))

    val currencyAmounts: Flowable<List<CurrencyUiModel>> =
        mainCurrencySubject
            .toFlowable(BackpressureStrategy.LATEST)
            .switchMap { (amount, currencyCode) ->
                val mainCurrency = Currency(currencyCode)
                calculateRatesUseCase.execute(Money(amount, mainCurrency))
                    .map { currencies ->
                        listOf(MainCurrency(mainCurrency, amount)) +
                                currencies.map { ConvertedCurrency(it) }
                    }
            }

    fun setMainCurrency(amount: String, currencyCode: String) {
        mainCurrencySubject.onNext(Pair(amount, currencyCode))
    }

    fun setNewMainCurrencyAmount(amount: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
