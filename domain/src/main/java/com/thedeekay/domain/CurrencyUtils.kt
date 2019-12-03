package com.thedeekay.domain

import java.util.*

// Commonly used currencies
val EUR = Currency("EUR")
val USD = Currency("USD")
val GBP = Currency("GBP")

/**
 * Creates a [Currency] with the given ISO 4217 currency code.
 * Will throw an exception if the code is invalid.
 */
@Suppress("FunctionName")
fun Currency(currencyCode: String): Currency = Currency.getInstance(currencyCode)!!

data class CurrencyPair(
    val base: Currency,
    val counter: Currency
) {

    /**
     * Creates [ExchangeRate] between base and counter currencies of this pair at the given [rate].
     */
    infix fun at(rate: Double): ExchangeRate {
        return ExchangeRate(base, counter, rate)
    }
}

operator fun Currency.div(other: Currency): CurrencyPair {
    return CurrencyPair(this, other)
}
