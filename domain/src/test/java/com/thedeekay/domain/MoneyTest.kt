package com.thedeekay.domain

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal

class MoneyTest {

    @Test(expected = IllegalArgumentException::class)
    fun `converting any currency using exchange rate where it is not base throws an exception`() {
        Money(0, GBP).convert(ExchangeRate(EUR, USD, 1))
    }

    @Test
    fun `converting 0 money at any rate makes 0 other currency`() {
        val result = Money(0, EUR).convert(to = USD, at = 1.11)

        assertThat(result, `is`(Money(0, USD)))
    }

    @Test
    fun `converting 2EUR to USD at 1,11 gives 2,22USD`() {
        val result = Money(2, EUR).convert(to = USD, at = 1.11)

        assertThat(result, `is`(Money(2.22, USD)))
    }

    @Test
    fun `conversion keeps high precision`() {
        val result = Money(43082.12, EUR).convert(to = USD, at = 1.1081723)

        assertThat(result, `is`(Money(47742.412009276, USD)))
    }

    @Test
    fun `same currency with same amount in different formats is equal`() {
        assertThat(Money(BigDecimal("7.5"), USD), `is`(Money(BigDecimal("7.50"), USD)))
    }

    @Test
    fun `different currency with same amount is not equal`() {
        assertThat(Money(7.5, USD), `is`(not(equalTo((Money(7.5, GBP))))))
    }

    @Test
    fun `double times currency syntax yields the same results as constructor`() {
        assertThat(2.0 * EUR, `is`(Money(2, EUR)))
    }
}
