package com.thedeekay.exchangerates

import dagger.Binds
import dagger.Module

@Module
internal interface ExchangeRatesRepositoryModule {

    @Binds
    fun bindExchangeRatesRepository(
        repository: DefaultExchangeRatesRepository
    ): ExchangeRatesRepository
}
