package com.thedeekay.currencies.imageloading

import dagger.Binds
import dagger.Module

@Module
interface CurrencyFlagLoaderModule {

    @Binds
    fun bindCurrencyFlagLoader(currencyFlagLoader: GlideCurrencyFlagLoader): CurrencyFlagLoader

}
