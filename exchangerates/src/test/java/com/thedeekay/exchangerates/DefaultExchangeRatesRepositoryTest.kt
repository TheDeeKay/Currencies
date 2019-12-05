package com.thedeekay.exchangerates

import com.thedeekay.domain.EUR
import org.junit.Test

class DefaultExchangeRatesRepositoryTest {

    @Test
    fun `exchange rates emit an empty list when nothing has been set`() {
        val repository = DefaultExchangeRatesRepository()

        repository.allExchangeRates(EUR).test()

            .assertValue(emptyList())
            .assertNotComplete()
    }

    @Test
    fun `exchange rates are empty when empty list is set`() {
        val repository = DefaultExchangeRatesRepository()
        repository.setExchangeRates(emptyList(), EUR)

        repository.allExchangeRates(EUR).test()

            .assertValue(emptyList())
            .assertNotComplete()
    }
}
