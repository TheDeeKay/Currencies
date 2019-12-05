package com.thedeekay.exchangerates

import com.thedeekay.networking.connectivity.ConnectivityChecker
import com.thedeekay.networking.connectivity.DefaultConnectivityChecker
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class, ConnectivityCheckerModule::class])
internal class ExchangeRatesNetworkModule {

    @Provides
    @Singleton
    fun provideExchangeRatesService(retrofit: Retrofit): ExchangeRatesService {
        return retrofit.create(ExchangeRatesService::class.java)
    }
}

@Module
internal interface ConnectivityCheckerModule {
    @Binds
    fun bindConnectivityChecker(
        connectivityChecker: DefaultConnectivityChecker
    ): ConnectivityChecker
}
