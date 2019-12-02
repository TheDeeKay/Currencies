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
}
