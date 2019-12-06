package com.thedeekay.currencies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.*
import com.thedeekay.exchangerates.CalculateRatesUseCase
import com.thedeekay.rxtestutils.RxJavaSchedulersRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO: see if it's possible to clear all this LiveData testing junk
class RatesViewModelTest {

    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var calculateRatesUseCase: CalculateRatesUseCase
    private lateinit var viewModel: RatesViewModel

    private lateinit var lifecycleOwner: FakeLifecycleOwner


    @Before
    fun setUp() {
        calculateRatesUseCase = mockk()

        viewModel = RatesViewModel(calculateRatesUseCase)

        lifecycleOwner = FakeLifecycleOwner()
    }

    @After
    fun tearDown() {
        lifecycleOwner.lifecycleRegistry.currentState = DESTROYED
    }

    @Test
    fun `initially selected currency is EUR and is returned when rates are empty`() {
        setCalculatedCurrencies(0L * EUR, emptyList())

        assertCurrencyAmounts(
            listOf(MainCurrency("EUR", "Euro"))
        )
    }

    @Test
    fun `when rates are present, currencies are properly mapped`() {
        viewModel.setMainCurrency(10L * EUR)
        setCalculatedCurrencies(
            10L * EUR,
            listOf(
                11.1 * USD,
                9.1 * GBP
            )
        )

        assertCurrencyAmounts(
            listOf(
                MainCurrency("EUR", "Euro"),
                ConvertedCurrency("USD", "US Dollar", "11.1"),
                ConvertedCurrency("GBP", "British Pound Sterling", "9.1")
            )
        )
    }

    @Test
    fun `changing main currency changes to proper calculated values`() {
        viewModel.setMainCurrency(5L * GBP)
        setCalculatedCurrencies(
            5L * GBP,
            listOf(
                4L * EUR,
                6.5 * USD
            )
        )

        assertCurrencyAmounts(
            listOf(
                MainCurrency("GBP", "British Pound Sterling"),
                ConvertedCurrency("EUR", "Euro", "4"),
                ConvertedCurrency("USD", "US Dollar", "6.5")
            )
        )
    }

    private fun assertCurrencyAmounts(list: List<CurrencyUiModel>) {
        assertThat(
            viewModel.currencyAmounts.value,
            `is`(
                list
            )
        )
    }

    private fun setCalculatedCurrencies(
        mainCurrency: Money,
        calculatedCurrencies: List<Money>
    ) {
        every { calculateRatesUseCase.execute(mainCurrency) }
            .returns(Flowable.just(calculatedCurrencies).mergeWith(Flowable.never()))

        // has to happen after use case is mocked, otherwise the livedata tries to use it before it's ready
        viewModel.currencyAmounts.observe(lifecycleOwner, Observer { })
        lifecycleOwner.lifecycleRegistry.currentState = STARTED
    }
}

private class FakeLifecycleOwner : LifecycleOwner {

    val lifecycleRegistry = LifecycleRegistry(this).apply { currentState = CREATED }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

}
