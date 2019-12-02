package com.thedeekay.domain

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal.*
import java.util.*

class MoneyTest {

    @Test(expected = IllegalArgumentException::class)
    fun `converting any currency using exchange rate where it is not base throws an exception`() {
        Money(ZERO, JPY).convert(ExchangeRate(EUR, USD, ONE))
    }

    @Test
    fun `converting 0 money at any rate makes 0 other currency`() {
        val result = Money(ZERO, EUR).convert(ExchangeRate(EUR, USD, ONE))

        assertThat(result.amount, `is`(ZERO))
    }

    @Test
    fun `converting 2EUR to USD at 1,11 gives 2,22USD`() {
        val result = Money(valueOf(2), EUR).convert(ExchangeRate(EUR, USD, valueOf(1.11)))

        assertThat(result, `is`(Money(valueOf(2.22), USD)))
    }

    @Test
    fun `conversion keeps high precision`() {
        val rate = valueOf(1.1081723)
        val baseAmount = valueOf(43082.12)
        val expectedAmount = valueOf(47742.412009276)

        val result = Money(baseAmount, EUR).convert(ExchangeRate(EUR, USD, rate))

        assertThat(result, `is`(Money(expectedAmount, USD)))
    }
}

private val USD = Currency.getInstance("USD")
private val JPY = Currency.getInstance("JPY")
private val EUR = Currency.getInstance("EUR")
