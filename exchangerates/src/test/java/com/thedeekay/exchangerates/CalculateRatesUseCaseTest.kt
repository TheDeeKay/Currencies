package com.thedeekay.exchangerates

import com.thedeekay.domain.*
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import java.util.*

class CalculateRatesUseCaseTest {

    private lateinit var getExchangeRatesUseCase: GetExchangeRatesUseCase
    private lateinit var calculateRatesUseCase: CalculateRatesUseCase

    @Before
    fun setUp() {
        getExchangeRatesUseCase = mockk()

        calculateRatesUseCase = CalculateRatesUseCase(getExchangeRatesUseCase)
    }

    @Test
    fun `should return empty list when no rates are known`() {
        setRatesForCurrency(EUR, emptyList())

        calculateRatesUseCase.execute(10L * EUR).test()

            .assertValue(emptyList())
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `should return 0 amounts for all currencies if original amount is 0`() {
        setRatesForCurrency(
            EUR,
            listOf(
                EUR / USD at 1.1082,
                EUR / GBP at 0.9201
            )
        )

        calculateRatesUseCase.execute(0L * EUR).test()

            .assertValue(listOf(0L * USD, 0L * GBP))
            .assertNoErrors()
            .assertNotComplete()
    }

    private fun setRatesForCurrency(
        currency: Currency,
        rates: List<ExchangeRate>
    ) {
        every { getExchangeRatesUseCase.execute(currency) }.returns(
            Flowable.just(rates)
                .mergeWith(Flowable.never())
        )
    }
}
