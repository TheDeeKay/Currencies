package com.thedeekay.domain

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class ExchangeRateTest {

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be negative`() {
        ExchangeRate(USD, EUR, BigDecimal(-1.12))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `exchange rate cannot be zero`() {
        ExchangeRate(USD, EUR, ZERO)
    }

    @Test
    fun `converting 5,0GBP at rate 1,5 to USD yields 7,5USD`() {
        assertThat(ExchangeRate(GBP, USD, 1.5).convert(5.0), `is`(Money(7.5, USD)))
    }

    @Test
    fun `base div counter at rate syntax yields the same results as the constructor`() {
        assertThat(EUR / USD at 1.1082, `is`(ExchangeRate(EUR, USD, 1.1082)))
    }
}
