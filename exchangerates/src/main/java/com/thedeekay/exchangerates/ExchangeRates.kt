package com.thedeekay.exchangerates

object ExchangeRates {

    val exchangeRatesApi: ExchangeRatesApi by lazy {
        DaggerExchangeRatesApiComponent.create()
    }

}
