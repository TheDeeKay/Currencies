package com.thedeekay.currencies

import com.thedeekay.exchangerates.ExchangeRatesApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [ExchangeRatesApi::class])
interface AppComponent {

    fun inject(ratesFragment: RatesFragment)

    @Component.Builder
    interface Builder {

        fun exchangeRatesApi(exchangeRatesApi: ExchangeRatesApi): Builder

        fun build(): AppComponent
    }

}
