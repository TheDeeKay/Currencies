package com.thedeekay.currencies

import android.app.Application
import com.thedeekay.exchangerates.ExchangeRates

class CurrenciesApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        ExchangeRates.init(this)
        appComponent = DaggerAppComponent.builder().exchangeRatesApi(ExchangeRates.api).build()
    }
}
