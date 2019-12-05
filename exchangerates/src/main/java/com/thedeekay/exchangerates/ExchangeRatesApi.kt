package com.thedeekay.exchangerates

import com.thedeekay.exchangerates.network.ExchangeRatesNetworkRequest
import com.thedeekay.exchangerates.repository.ExchangeRatesRepository

/**
 * API for the exchangerates module.
 */
interface ExchangeRatesApi {

    fun exchangeRatesNetworkRequest(): ExchangeRatesNetworkRequest

    fun exchangeRatesRepository(): ExchangeRatesRepository

}
