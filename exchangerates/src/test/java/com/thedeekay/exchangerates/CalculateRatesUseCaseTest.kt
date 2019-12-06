package com.thedeekay.exchangerates

import com.thedeekay.domain.*
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

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
        every { getExchangeRatesUseCase.execute(EUR) }.returns(
            Flowable.just(emptyList<ExchangeRate>())
                .mergeWith(Flowable.never())
        )

        calculateRatesUseCase.execute(10L * EUR).test()

            .assertValue(emptyList())
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun `should return 0 amounts for all currencies if original amount is 0`() {
        every { getExchangeRatesUseCase.execute(EUR) }.returns(
            Flowable.just(
                listOf(
                    EUR / USD at 1.1082,
                    EUR / GBP at 0.9201
                )
            )
                .mergeWith(Flowable.never())
        )

        calculateRatesUseCase.execute(0L * EUR).test()

            .assertValue(listOf(0L * USD, 0L * GBP))
            .assertNoErrors()
            .assertNotComplete()
    }
}
