package com.thedeekay.exchangerates

import org.junit.Test

class DefaultExchangeRatesRepositoryTest {

    @Test
    fun `exchange rates are empty when empty list is set`() {
        val repository = DefaultExchangeRatesRepository()
        repository.setExchangeRates(emptyList())

        repository.allExchangeRates().test()

            .assertValue(emptyList())
            .assertNotComplete()
    }
}
