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
        setCalculatedCurrencies(10L * EUR, emptyList())

        assertThat(
            viewModel.currencyAmounts.value,
            `is`(listOf<CurrencyUiModel>(MainCurrency("EUR", "Euro")))
        )
    }

    @Test
    fun `when rates are present, currencies are properly mapped`() {
        setCalculatedCurrencies(
            10L * EUR,
            listOf(
                11.1 * USD,
                9.1 * GBP
            )
        )

        assertThat(
            viewModel.currencyAmounts.value,
            `is`(
                listOf(
                    MainCurrency("EUR", "Euro"),
                    ConvertedCurrency("USD", "US Dollar", "11.1"),
                    ConvertedCurrency("GBP", "British Pound Sterling", "9.1")
                )
            )
        )
    }

    private fun setCalculatedCurrencies(
        mainCurrency: Money,
        calculatedCurrencies: List<Money>
    ) {
        every { calculateRatesUseCase.execute(mainCurrency) }
            .returns(Flowable.just(calculatedCurrencies).mergeWith(Flowable.never()))
        viewModel.currencyAmounts.observe(lifecycleOwner, Observer { })
        lifecycleOwner.lifecycleRegistry.currentState = STARTED
    }
}

private class FakeLifecycleOwner : LifecycleOwner {

    val lifecycleRegistry = LifecycleRegistry(this).apply { currentState = CREATED }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

}
