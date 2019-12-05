package com.thedeekay.exchangerates

import android.content.Context
import com.thedeekay.exchangerates.network.RetrofitModule

object ExchangeRates {

    private lateinit var exchangeRatesApi: ExchangeRatesApi

    /**
     * Get a ready-to-use instance of [ExchangeRatesApi].
     *
     * MUST call [init] before attempting to use this.
     */
    val api: ExchangeRatesApi
        get() = exchangeRatesApi

    /**
     * Initialize this module and prepare it for use.
     */
    fun init(context: Context) {

        DaggerExchangeRatesApiComponent
            .builder()
            .bindContext(context)
            .retrofitModule(
                RetrofitModule(
                    BASE_URL
                )
            )
            .build()
    }

}

private const val BASE_URL = "https://revolut.duckdns.org/"
