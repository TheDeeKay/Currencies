package com.thedeekay.currencies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.*
import com.thedeekay.exchangerates.CalculateRatesUseCase
import com.thedeekay.rxtestutils.RxJavaSchedulersRule
import com.thedeekay.rxtestutils.assertLatestValue
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Ignore
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

        assertLatestCurrencyAmounts(
            MainCurrency("EUR", "Euro", "")
        )
    }

    @Test
    fun `when rates are present, currencies are properly mapped`() {
        viewModel.setMainCurrency("10.172", "EUR")
        setConvertedCurrenciesForMainCurrency(
            10.172 * EUR,
            listOf(
                11.1 * USD,
                9.1 * GBP
            )
        )

        assertLatestCurrencyAmounts(
            MainCurrency("EUR", "Euro", "10.172"),
            ConvertedCurrency("USD", "US Dollar", "11.10"),
            ConvertedCurrency("GBP", "British Pound Sterling", "9.10")
        )
    }

    @Test
    fun `changing main currency changes to proper calculated values`() {
        viewModel.setMainCurrency("5.5", "GBP")
        setConvertedCurrenciesForMainCurrency(
            5.5 * GBP,
            listOf(
                6L * EUR,
                7.553 * USD
            )
        )

        assertLatestCurrencyAmounts(
            MainCurrency("GBP", "British Pound Sterling", "5.5"),
            ConvertedCurrency("EUR", "Euro", "6.00"),
            ConvertedCurrency("USD", "US Dollar", "7.55")
        )
    }

    @Test
    fun `changing main currency amount recalculates other currencies`() {
        setConvertedCurrenciesForMainCurrency(
            17.1 * EUR,
            listOf(
                1731.132 * JPY,
                15.3 * GBP
            )
        )
        viewModel.setNewMainCurrencyAmount("17.1")

        assertLatestCurrencyAmounts(
            MainCurrency("EUR", "Euro", "17.1"),
            ConvertedCurrency("JPY", "Japanese Yen", "1731"),
            ConvertedCurrency("GBP", "British Pound Sterling", "15.30")
        )
    }

    @Ignore("Can't make this one work")
    @Test
    fun `changing main currency immediately swaps its position without waiting for recalculation`() {
        viewModel.setMainCurrency("5", "GBP")
        setConvertedCurrenciesForMainCurrency(
            5L * GBP,
            listOf(
                4L * EUR,
                6.5 * USD
            )
        )
        viewModel.setMainCurrency("4", "EUR")

        assertLatestCurrencyAmounts(
            MainCurrency("EUR", "Euro", "4"),
            ConvertedCurrency("GBP", "British Pound Sterling", "5.00"),
            ConvertedCurrency("USD", "US Dollar", "6.50")
        )
    }

    private fun assertLatestCurrencyAmounts(vararg currencies: CurrencyUiModel) {
        viewModel.currencyAmounts.test()
            .assertLatestValue(currencies.toList())
            .assertNoErrors()
            .assertNotComplete()
    }

    private fun setConvertedCurrenciesForMainCurrency(
        mainCurrency: Money,
        calculatedCurrencies: List<Money>
    ) {
        every { calculateRatesUseCase.execute(any()) }.returns(Flowable.never())
        every { calculateRatesUseCase.execute(mainCurrency) }
            .returns(Flowable.just(calculatedCurrencies).mergeWith(Flowable.never()))
    }
}
