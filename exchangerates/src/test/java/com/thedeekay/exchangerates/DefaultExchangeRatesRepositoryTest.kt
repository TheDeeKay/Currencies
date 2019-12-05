package com.thedeekay.exchangerates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.thedeekay.domain.EUR
import com.thedeekay.domain.GBP
import com.thedeekay.domain.USD
import com.thedeekay.domain.div
import com.thedeekay.rxtestutils.RxJavaSchedulersRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DefaultExchangeRatesRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()

    private lateinit var repository: DefaultExchangeRatesRepository

    @Before
    fun setUp() {
        val exchangeRatesDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ExchangeRatesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        repository = DefaultExchangeRatesRepository(exchangeRatesDatabase)
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

    @Test
    fun `storing several exchange rates should emit those same exchange rates`() {
        val exchangeRates = listOf(
            EUR / USD at 1.1082,
            EUR / GBP at 0.8103
        )
        repository.setExchangeRates(exchangeRates, EUR)

        repository.allExchangeRates(EUR).test()

            .assertValue { it.sameElementsAs(exchangeRates) }
            .assertNotComplete()
    }

    private fun <T> List<T>.sameElementsAs(
        other: List<T>
    ) = containsAll(other) && other.containsAll(this)
}
