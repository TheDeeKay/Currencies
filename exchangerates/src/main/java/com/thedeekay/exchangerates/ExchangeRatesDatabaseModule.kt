package com.thedeekay.exchangerates

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides

@Module
internal class ExchangeRatesDatabaseModule {

    @Provides
    fun provideExchangeRatesDatabase(context: Context): ExchangeRatesDatabase {
        return Room.databaseBuilder(
            context,
            ExchangeRatesDatabase::class.java,
            "ExchangeRates"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}
