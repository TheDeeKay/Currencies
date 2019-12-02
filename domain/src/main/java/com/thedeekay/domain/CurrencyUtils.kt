package com.thedeekay.domain

import java.util.*

val EUR = Currency("EUR")
val USD = Currency("USD")
val GBP = Currency("GBP")

/**
 * Creates a [Currency] with the given ISO 4217 currency code.
 * Will throw an exception if the code is invalid.
 */
@Suppress("FunctionName")
fun Currency(currencyCode: String): Currency = Currency.getInstance(currencyCode)!!
