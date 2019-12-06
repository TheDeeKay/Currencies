package com.thedeekay.currencies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.EUR
import com.thedeekay.domain.Money
import com.thedeekay.domain.times
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

class RatesViewModelTest {

    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var calculateRatesUseCase: CalculateRatesUseCase
    private lateinit var viewModel: RatesViewModel

    private lateinit var observer: Observer<List<CurrencyUiModel>>

    @Before
    fun setUp() {
        calculateRatesUseCase = mockk()

        viewModel = RatesViewModel(calculateRatesUseCase)

        viewModel.currencyAmounts.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.currencyAmounts.removeObserver(observer)
    }

    @Test
    fun `initially selected currency is EUR`() {
        every { calculateRatesUseCase.execute(0L * EUR) }
            .returns(Flowable.just(emptyList<Money>()).mergeWith(Flowable.never()))

        assertThat(
            viewModel.currencyAmounts.value,
            `is`(listOf<CurrencyUiModel>(MainCurrency("EUR", "Euro")))
        )
    }
}
