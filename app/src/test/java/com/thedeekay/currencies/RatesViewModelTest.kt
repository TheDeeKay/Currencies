package com.thedeekay.currencies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.*
import com.thedeekay.exchangerates.CalculateRatesUseCase
import com.thedeekay.rxtestutils.RxJavaSchedulersRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
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


    @Before
    fun setUp() {
        calculateRatesUseCase = mockk()

        viewModel = RatesViewModel(calculateRatesUseCase)
    }

    @Test
    fun `initially selected currency is EUR and is returned when rates are empty`() {
        setConvertedCurrenciesForMainCurrency(
            0L * EUR,
            emptyList()
        )

        assertCurrencyAmounts(
            MainCurrency("EUR", "Euro")
        )
    }

    @Test
    fun `when rates are present, currencies are properly mapped`() {
        viewModel.setMainCurrency(10L * EUR)
        setConvertedCurrenciesForMainCurrency(
            10L * EUR,
            listOf(
                11.1 * USD,
                9.1 * GBP
            )
        )

        assertCurrencyAmounts(
            MainCurrency("EUR", "Euro"),
            ConvertedCurrency("USD", "US Dollar", "11.1"),
            ConvertedCurrency("GBP", "British Pound Sterling", "9.1")
        )
    }

    @Test
    fun `changing main currency changes to proper calculated values`() {
        viewModel.setMainCurrency(5L * GBP)
        setConvertedCurrenciesForMainCurrency(
            5L * GBP,
            listOf(
                4L * EUR,
                6.5 * USD
            )
        )

        assertCurrencyAmounts(
            MainCurrency("GBP", "British Pound Sterling"),
            ConvertedCurrency("EUR", "Euro", "4"),
            ConvertedCurrency("USD", "US Dollar", "6.5")
        )
    }

    private fun assertCurrencyAmounts(vararg currencies: CurrencyUiModel) {
        viewModel.currencyAmounts.test()
            .assertValues(currencies.toList())
            .assertNoErrors()
            .assertNotComplete()
    }

    private fun setConvertedCurrenciesForMainCurrency(
        mainCurrency: Money,
        calculatedCurrencies: List<Money>
    ) {
        every { calculateRatesUseCase.execute(mainCurrency) }
            .returns(Flowable.just(calculatedCurrencies).mergeWith(Flowable.never()))
    }
}