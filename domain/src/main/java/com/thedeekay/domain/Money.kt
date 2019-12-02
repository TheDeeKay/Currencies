package com.thedeekay.domain

import java.math.BigDecimal

/**
 * Represents a certain amount of specific currency.
 */
data class Money(
    val amount: BigDecimal,
    val currency: Currency
)
