package com.thedeekay.domain

import java.math.BigDecimal
import java.util.*

/**
 * Exchange rate between two currencies.
 *
 * Principally consists of three components: [base] currency, [counter] currency, and the
 * exchange [rate] between the two.
 * Base currency is the currency which you can buy by selling the counter currency
 * at the given rate.
 *
 * For instance, exchange rate of EUR/USD is 1.1082.
 * In this example, base currency is EUR, counter currency is USD, and rate is 1.1082.
 * This means you can buy 1.1082USD by selling 1EUR, and vice-versa.
 */
// TODO: seems that most places that create this are creating by converting Double to BigDecimal, which loses precision
// create by converting String to BigDecimal instead
data class ExchangeRate(
    val base: Currency,
    val counter: Currency,
    val rate: BigDecimal
) {

    constructor(base: Currency, counter: Currency, rate: Double) :
            this(base, counter, BigDecimal.valueOf(rate))

    constructor(base: Currency, counter: Currency, rate: Long) :
            this(base, counter, BigDecimal.valueOf(rate))

    /**
     * Converts [amount] of [base] to [counter] at the given [rate].
     *
     * For instance, if this exchange rate is EUR/USD at 1.1, this method will convert [amount] of
     * EUR to [amount]*1.1 of USD.
     */
    fun convert(amount: Double): Money = Money(amount, base).convert(this)

    init {
        require(rate > BigDecimal.ZERO) { "Rate must be greater than zero!" }
    }
}
