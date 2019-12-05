package com.thedeekay.exchangerates

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ExchangeRatesNetworkModule::class])
internal interface ExchangeRatesApiComponent : ExchangeRatesApi
