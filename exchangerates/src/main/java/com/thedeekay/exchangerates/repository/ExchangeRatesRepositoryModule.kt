package com.thedeekay.exchangerates.repository

import com.thedeekay.exchangerates.repository.DefaultExchangeRatesRepository
import com.thedeekay.exchangerates.repository.ExchangeRatesRepository
import dagger.Binds
import dagger.Module

@Module
internal interface ExchangeRatesRepositoryModule {

    @Binds
    fun bindExchangeRatesRepository(
        repository: DefaultExchangeRatesRepository
    ): ExchangeRatesRepository
}
