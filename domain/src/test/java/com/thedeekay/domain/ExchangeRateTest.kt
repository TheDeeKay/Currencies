package com.thedeekay.domain

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal

class ExchangeRateTest {

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be negative`() {
        ExchangeRate(USD, EUR, BigDecimal(-1.12))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be zero`() {
        ExchangeRate(USD, EUR, BigDecimal(0))
    }

    @Test
    fun `converting 5,0GBP at rate 1,5 to USD yields 7,5USD`() {
        assertThat(ExchangeRate(GBP, USD, 1.5).convert(5.0), `is`(Money(7.5, USD)))
    }
}
