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
data class ExchangeRate(
    val base: Currency,
    val counter: Currency,
    val rate: BigDecimal
)
