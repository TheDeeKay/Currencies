package com.thedeekay.domain

import org.junit.Test
import java.math.BigDecimal
import java.util.*

class ExchangeRateTest {

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be negative`() {
        ExchangeRate(Currency.getInstance("USD"), Currency.getInstance("EUR"), BigDecimal(-1.12))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be zero`() {
        ExchangeRate(Currency.getInstance("USD"), Currency.getInstance("EUR"), BigDecimal(-1.12))
    }
}
