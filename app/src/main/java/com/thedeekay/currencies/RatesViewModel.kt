package com.thedeekay.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thedeekay.exchangerates.CalculateRatesUseCase

/**
 * View model for the currencies conversion screen.
 */
class RatesViewModel(calculateRatesUseCase: CalculateRatesUseCase) : ViewModel() {

    val currencyAmounts: LiveData<List<CurrencyUiModel>> = MutableLiveData(
        listOf(
            CurrencyUiModel.MainCurrency("EUR", "Euro")
        )
    )
}
