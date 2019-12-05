package com.thedeekay.exchangerates

/**
 * API for the exchangerates module.
 */
interface ExchangeRatesApi {

    fun exchangeRatesNetworkRequest(): ExchangeRatesNetworkRequest

    fun exchangeRatesRepository(): ExchangeRatesRepository

}
