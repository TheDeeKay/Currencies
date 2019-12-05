package com.thedeekay.exchangerates

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {
    @TypeConverter
    fun fromBigDecimal(bigDecimal: BigDecimal): String = bigDecimal.toEngineeringString()

    @TypeConverter
    fun toBigDecimal(string: String): BigDecimal = BigDecimal(string)
}
