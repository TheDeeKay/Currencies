package com.thedeekay.exchangerates

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ExchangeRatesNetworkModule::class,
    ExchangeRatesRepositoryModule::class,
    ExchangeRatesDatabaseModule::class
])
internal interface ExchangeRatesApiComponent : ExchangeRatesApi {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindContext(context: Context): Builder

        fun retrofitModule(retrofitModule: RetrofitModule): Builder

        fun build(): ExchangeRatesApiComponent
    }

}
