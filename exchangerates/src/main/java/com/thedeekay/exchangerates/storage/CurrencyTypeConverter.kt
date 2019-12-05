package com.thedeekay.exchangerates.storage

import androidx.room.TypeConverter
import com.thedeekay.domain.Currency
import java.util.*

/**
 * Room type converters for exchange rate related classes.
 */

class CurrencyTypeConverter {
    @TypeConverter
    fun fromCurrency(currency: Currency): String = currency.currencyCode

    @TypeConverter
    fun toCurrency(currencyCode: String): Currency =
        Currency(currencyCode)
}
