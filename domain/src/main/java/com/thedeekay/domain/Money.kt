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
    fun convert(exchangeRate: ExchangeRate): Money {
        throw IllegalArgumentException()
    }
}
