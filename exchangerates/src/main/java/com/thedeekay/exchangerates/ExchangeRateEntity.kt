package com.thedeekay.exchangerates

import androidx.room.Entity
import com.thedeekay.exchangerates.ExchangeRateEntity.Companion.TABLE
import java.math.BigDecimal
import java.util.*

@Entity(tableName = TABLE, primaryKeys = ["base", "counter"])
data class ExchangeRateEntity(
    val base: Currency,
    val counter: Currency,
    val rate: BigDecimal
) {
    companion object {
        const val TABLE = "exchangeRate"
    }
}
