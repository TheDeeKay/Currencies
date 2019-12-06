package com.thedeekay.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.EUR
import com.thedeekay.domain.Money
import com.thedeekay.exchangerates.CalculateRatesUseCase
import java.math.BigDecimal

/**
 * View model for the currencies conversion screen.
 */
class RatesViewModel(private val calculateRatesUseCase: CalculateRatesUseCase) : ViewModel() {

    val currencyAmounts: LiveData<List<CurrencyUiModel>> by lazy {
        calculateRatesUseCase.execute(Money(BigDecimal("10"), EUR))
            .map { currencies ->
                listOf(MainCurrency(EUR.currencyCode, EUR.displayName)) +
                        currencies.map {
                            ConvertedCurrency(
                                it.currency.currencyCode,
                                it.currency.displayName,
                                it.amount.toString()
                            )
                        }
            }
            .toLiveData()
    }
}
