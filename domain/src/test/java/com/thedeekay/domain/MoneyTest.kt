package com.thedeekay.domain

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import java.util.*

class MoneyTest {

    @Test(expected = IllegalArgumentException::class)
    fun `converting any currency using exchange rate where it is not base throws an exception`() {
        Money(ZERO, EUR).convert(ExchangeRate(USD, JPY, ONE))
    }

    @Test
    fun `converting 0 money at any rate makes 0 other currency`() {
        val result = Money(ZERO, EUR).convert(ExchangeRate(EUR, USD, ONE))

        assertThat(result.amount, `is`(ZERO))
    }

}

private val USD = Currency.getInstance("USD")
private val JPY = Currency.getInstance("JPY")
private val EUR = Currency.getInstance("EUR")
