package com.thedeekay.exchangerates

/**
 * API for the exchangerates module.
 */
interface ExchangeRatesApi {

    fun getExchangeRatesUseCase(): GetExchangeRatesUseCase

    fun calculateRatesUseCase(): CalculateRatesUseCase

}
