package com.thedeekay.exchangerates

object ExchangeRates {

    val exchangeRatesApi: ExchangeRatesApi by lazy {
        DaggerExchangeRatesApiComponent
            .builder()
            .retrofitModule(RetrofitModule(BASE_URL))
            .build()
    }

}

private const val BASE_URL = "https://revolut.duckdns.org/"
