package com.thedeekay.domain

import java.math.BigDecimal
import java.util.*

/**
 * Represents a certain amount of specific currency.
 */
data class Money(
    val amount: BigDecimal,
    val currency: Currency
) {

    constructor(amount: Double, currency: Currency) : this(BigDecimal.valueOf(amount), currency)

    constructor(amount: Long, currency: Currency) : this(BigDecimal.valueOf(amount), currency)

    fun convert(exchangeRate: ExchangeRate): Money {
        if (exchangeRate.base != currency) {
            throw IllegalArgumentException("Base currency must be this Money's currency")
        }

        return Money(amount.multiply(exchangeRate.rate), exchangeRate.counter)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money

        if (amount.compareTo(other.amount) != 0) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount.hashCode()
        result = 31 * result + currency.hashCode()
        return result
    }
}
