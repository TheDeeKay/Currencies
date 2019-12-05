package com.thedeekay.exchangerates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.*
import com.thedeekay.networking.NetworkFailure.Generic.Unknown
import com.thedeekay.rxtestutils.assertValueHasSameElementsAs
import com.thedeekay.rxtestutils.assertValuesHaveSameElementsAs
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit.SECONDS

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DefaultExchangeRatesRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var computationTestScheduler: TestScheduler
    private var originalComputationHandler: Function<in Scheduler, out Scheduler>? = null

    private lateinit var exchangeRatesNetworkRequest: ExchangeRatesNetworkRequest
    private lateinit var repository: DefaultExchangeRatesRepository

    @Before
    fun setUp() {
        computationTestScheduler = TestScheduler()
        originalComputationHandler = RxJavaPlugins.getComputationSchedulerHandler()
        RxJavaPlugins.setComputationSchedulerHandler { computationTestScheduler }

        val exchangeRatesDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ExchangeRatesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        exchangeRatesNetworkRequest = mockk()
        every { exchangeRatesNetworkRequest.execute(any()) }.returns(Single.just(Failure(Unknown)))

        repository =
            DefaultExchangeRatesRepository(exchangeRatesDatabase, exchangeRatesNetworkRequest)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.setComputationSchedulerHandler(originalComputationHandler)
    }

    ///////////////////////////////
    // BASIC LOCAL STORAGE TESTS //
    ///////////////////////////////

    @Test
    fun `exchange rates emit an empty list when nothing has been set`() {
        repository.allExchangeRates(EUR).test()

            .assertValue(emptyList())
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `exchange rates are empty when empty list is set`() {
        repository.setExchangeRates(emptyList(), EUR)

        repository.allExchangeRates(EUR).test()

            .assertValue(emptyList())
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `storing several exchange rates should emit those same exchange rates`() {
        repository.setExchangeRates(EUR_EXCHANGE_RATES, EUR)

        repository.allExchangeRates(EUR).test()

            .assertValueHasSameElementsAs(EUR_EXCHANGE_RATES)
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `storing new rates should clear existing ones for the same base`() {
        repository.setExchangeRates(EUR_EXCHANGE_RATES, EUR)
        repository.setExchangeRates(EUR_EXCHANGE_RATES2, EUR)

        repository.allExchangeRates(EUR).test()

            .assertValueHasSameElementsAs(EUR_EXCHANGE_RATES2)
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `storing new rates should not clear existing ones for different base`() {
        repository.setExchangeRates(EUR_EXCHANGE_RATES, EUR)
        repository.setExchangeRates(USD_EXCHANGE_RATES, USD)

        repository.allExchangeRates(EUR).test()

            .assertValueHasSameElementsAs(EUR_EXCHANGE_RATES)
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `storing new rates emits them immediately`() {
        repository.setExchangeRates(EUR_EXCHANGE_RATES, EUR)
        val testSubscriber = repository.allExchangeRates(EUR).test()

        repository.setExchangeRates(EUR_EXCHANGE_RATES2, EUR)

        testSubscriber
            .assertValuesHaveSameElementsAs(EUR_EXCHANGE_RATES, EUR_EXCHANGE_RATES2)
            .assertNoErrors()
            .assertNotComplete()
    }


    ////////////////////////////
    // NETWORK-FETCHING TESTS //
    ////////////////////////////

    @Test
    fun `subscribing to repository should emit current rates and fetch rates for the given base`() {
        repository.setExchangeRates(EUR_EXCHANGE_RATES, EUR)
        every { exchangeRatesNetworkRequest.execute(ExchangeRatesRequestParams(EUR)) }
            .returns(Single.just(Success(EUR_EXCHANGE_RATES2)))

        repository.allExchangeRates(EUR).test()

            .assertValuesHaveSameElementsAs(EUR_EXCHANGE_RATES, EUR_EXCHANGE_RATES2)
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `repository should fetch rates every 1 second`() {
        every { exchangeRatesNetworkRequest.execute(ExchangeRatesRequestParams(EUR)) }
            .returnsMany(
                Single.just(Success(EUR_EXCHANGE_RATES)),
                Single.just(Success(EUR_EXCHANGE_RATES2))
            )
        val testSubscriber = repository.allExchangeRates(EUR).test()

        computationTestScheduler.advanceTimeBy(1, SECONDS)

        testSubscriber
            .assertValuesHaveSameElementsAs(emptyList(), EUR_EXCHANGE_RATES, EUR_EXCHANGE_RATES2)
            .assertNoErrors()
            .assertNotComplete()
    }
}
// TODO: see how errors should be propagated from the repository

private val EUR_EXCHANGE_RATES = listOf(
    EUR / USD at 1.1082,
    EUR / GBP at 0.8103,
    EUR / CHF at 1.0213
)

private val EUR_EXCHANGE_RATES2 = listOf(
    EUR / USD at 1.1100,
    EUR / RUB at 79.814,
    EUR / JPY at 129.94
)

private val USD_EXCHANGE_RATES = listOf(
    USD / EUR at 0.85906,
    USD / GBP at 0.77164,
    USD / CHF at 0.9686
)
