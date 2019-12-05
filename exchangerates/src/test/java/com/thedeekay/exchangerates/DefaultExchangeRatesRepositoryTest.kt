package com.thedeekay.exchangerates

import com.thedeekay.domain.EUR
import org.junit.Before
import org.junit.Test

class DefaultExchangeRatesRepositoryTest {

    private lateinit var repository: DefaultExchangeRatesRepository

    @Before
    fun setUp() {
        repository = DefaultExchangeRatesRepository()
    }

    @Test
    fun `exchange rates emit an empty list when nothing has been set`() {
        repository.allExchangeRates(EUR).test()

            .assertValue(emptyList())
            .assertNotComplete()
    }

    @Test
    fun `exchange rates are empty when empty list is set`() {
        repository.setExchangeRates(emptyList(), EUR)

        repository.allExchangeRates(EUR).test()

            .assertValue(emptyList())
            .assertNotComplete()
    }
}
