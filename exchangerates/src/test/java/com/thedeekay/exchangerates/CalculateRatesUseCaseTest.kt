package com.thedeekay.exchangerates

import com.thedeekay.domain.EUR
import com.thedeekay.domain.ExchangeRate
import com.thedeekay.domain.times
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Test

class CalculateRatesUseCaseTest {

    @Test
    fun `should return empty list when no rates are known`() {
        val getExchangeRatesUseCase = mockk<GetExchangeRatesUseCase>()
        every { getExchangeRatesUseCase.execute(EUR) }.returns(
            Flowable.just(emptyList<ExchangeRate>())
                .mergeWith(Flowable.never())
        )
        val calculateRatesUseCase = CalculateRatesUseCase(getExchangeRatesUseCase)

        calculateRatesUseCase.execute(10L * EUR).test()

            .assertValue(emptyList())
            .assertNoErrors()
            .assertNotComplete()
    }
}
