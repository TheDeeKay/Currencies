package com.thedeekay.exchangerates

import com.thedeekay.domain.ExchangeRate
import com.thedeekay.exchangerates.repository.ExchangeRatesRepository
import io.reactivex.Flowable
import java.util.*
import javax.inject.Inject

/**
 * Continuously gets latest exchange rates for the given base currency.
 */
class GetExchangeRatesUseCase @Inject internal constructor(
    private val exchangeRatesRepository: ExchangeRatesRepository
) {

    fun execute(base: Currency): Flowable<List<ExchangeRate>> =
        exchangeRatesRepository.allExchangeRates(base)

}
