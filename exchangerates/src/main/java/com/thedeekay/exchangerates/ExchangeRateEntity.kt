package com.thedeekay.exchangerates

import androidx.room.Entity
import com.thedeekay.domain.ExchangeRate
import com.thedeekay.exchangerates.ExchangeRateEntity.Companion.TABLE
import java.math.BigDecimal
import java.util.*

@Entity(tableName = TABLE, primaryKeys = ["base", "counter"])
data class ExchangeRateEntity(
    val base: Currency,
    val counter: Currency,
    val rate: BigDecimal
) {

    constructor(exchangeRate: ExchangeRate) : this(
        exchangeRate.base,
        exchangeRate.counter,
        exchangeRate.rate
    )

    fun toDomain(): ExchangeRate = ExchangeRate(base, counter, rate)

    companion object {
        const val TABLE = "exchangeRate"
    }
}
