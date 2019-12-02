package com.thedeekay.domain

import org.junit.Test
import java.math.BigDecimal

class ExchangeRateTest {

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be negative`() {
        ExchangeRate(USD, EUR, BigDecimal(-1.12))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be zero`() {
        ExchangeRate(USD, EUR, BigDecimal(-1.12))
    }
}
