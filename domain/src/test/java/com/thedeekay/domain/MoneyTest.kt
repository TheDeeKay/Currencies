package com.thedeekay.domain

import org.junit.Test
import java.math.BigDecimal
import java.util.*

class MoneyTest {

    @Test(expected = IllegalArgumentException::class)
    fun `converting any currency using exchange rate where it is not base throws an exception`() {
        Money(BigDecimal.ZERO, Currency.getInstance("EUR"))
            .convert(ExchangeRate(Currency.getInstance("USD"), Currency.getInstance("JPY"), BigDecimal.ONE))
    }
}
