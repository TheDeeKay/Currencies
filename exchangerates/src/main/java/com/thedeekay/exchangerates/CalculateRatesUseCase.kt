package com.thedeekay.exchangerates

import com.thedeekay.domain.Money
import io.reactivex.Flowable

/**
 * Continuously calculates what amount of every other currency a given currency amount
 * could be exchanged for, using latest exchange rates.
 *
 * For instance: given 10USD, and rates for USD/GBP and USD/EUR, it will continuously calculate
 * how much GBP and how much EUR the given 10USD amount to.
 */
class CalculateRatesUseCase(private val exchangeRatesUseCase: GetExchangeRatesUseCase) {

    fun execute(conversionAmount: Money): Flowable<List<Money>> {
        return exchangeRatesUseCase.execute(conversionAmount.currency)
            .map { rates ->
                rates.map { rate -> conversionAmount.convert(rate) }
            }
    }

}
