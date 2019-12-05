package com.thedeekay.exchangerates

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ExchangeRateEntity::class],
    version = 1
)
@TypeConverters(CurrencyTypeConverter::class, BigDecimalTypeConverter::class)
abstract class ExchangeRatesDatabase : RoomDatabase() {
    abstract fun exchangeRates(): ExchangeRatesDao
}
