package com.thedeekay.exchangerates

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
internal class ExchangeRatesNetworkModule {
    
    @Provides
    @Singleton
    fun bindExchangeRatesService(retrofit: Retrofit): ExchangeRatesService {
        return retrofit.create(ExchangeRatesService::class.java)
    }
    
}
