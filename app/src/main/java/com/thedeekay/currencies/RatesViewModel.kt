package com.thedeekay.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.EUR
import com.thedeekay.domain.Money
import com.thedeekay.domain.times
import com.thedeekay.exchangerates.CalculateRatesUseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject

/**
 * View model for the currencies conversion screen.
 */
class RatesViewModel(private val calculateRatesUseCase: CalculateRatesUseCase) : ViewModel() {

    private val mainCurrencySubject = BehaviorSubject.createDefault(0L * EUR)

    val currencyAmounts: LiveData<List<CurrencyUiModel>> by lazy {
        mainCurrencySubject.switchMap { mainCurrency ->
            calculateRatesUseCase.execute(mainCurrency)
                .map { currencies ->
                    listOf(MainCurrency(mainCurrency.currency)) +
                            currencies.map {
                                ConvertedCurrency(
                                    it.currency.currencyCode,
                                    it.currency.displayName,
                                    it.amount.toString() // TODO: more proper formatting
                                )
                            }
                }
                .toObservable()
        }
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
    }

    fun setMainCurrency(mainCurrency: Money) {
        mainCurrencySubject.onNext(mainCurrency)
    }
}
