package com.thedeekay.exchangerates

import android.content.Context
import com.thedeekay.exchangerates.network.ExchangeRatesNetworkModule
import com.thedeekay.exchangerates.network.RetrofitModule
import com.thedeekay.exchangerates.repository.ExchangeRatesRepositoryModule
import com.thedeekay.exchangerates.storage.ExchangeRatesDatabaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ExchangeRatesNetworkModule::class,
    ExchangeRatesRepositoryModule::class,
    ExchangeRatesDatabaseModule::class
])
internal interface ExchangeRatesApiComponent :
    ExchangeRatesApi {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindContext(context: Context): Builder

        fun retrofitModule(retrofitModule: RetrofitModule): Builder

        fun build(): ExchangeRatesApiComponent
    }

}
