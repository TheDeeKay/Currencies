package com.thedeekay.exchangerates.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ExchangeRateEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CurrencyTypeConverter::class, BigDecimalTypeConverter::class)
internal abstract class ExchangeRatesDatabase : RoomDatabase() {
    abstract fun exchangeRates(): ExchangeRatesDao
}
