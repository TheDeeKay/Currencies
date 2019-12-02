package com.thedeekay.domain

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class MoneyTest {

    @Test(expected = IllegalArgumentException::class)
    fun `converting any currency using exchange rate where it is not base throws an exception`() {
        Money(BigDecimal.ZERO, Currency.getInstance("EUR"))
            .convert(ExchangeRate(Currency.getInstance("USD"), Currency.getInstance("JPY"), BigDecimal.ONE))
    }

    @Test
    fun `converting 0 money at any rate makes 0 other currency`() {
        val result = Money(BigDecimal.ZERO, Currency.getInstance("EUR"))
            .convert(ExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("USD"), BigDecimal.ONE))

        assertThat(result.amount, `is`(BigDecimal.ZERO))
    }
}
