package com.thedeekay.currencies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class RatesViewModelTest {

    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `initially selected currency is EUR`() {
        val calculateRatesUseCase = mockk<CalculateRatesUseCase>()
        every { calculateRatesUseCase.execute(0L * EUR) }
            .returns(Flowable.just(emptyList<Money>()).mergeWith(Flowable.never()))
        val viewModel = RatesViewModel(calculateRatesUseCase)
        viewModel.currencyAmounts.observeForever { }

        assertThat(
            viewModel.currencyAmounts.value,
            `is`(listOf<CurrencyUiModel>(MainCurrency("EUR", "Euro")))
        )
    }
}
