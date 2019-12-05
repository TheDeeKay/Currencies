package com.thedeekay.exchangerates

import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable
import java.util.*

/**
 * Repository that manipulates storage and fetching of exchange rates.
 */
interface ExchangeRatesRepository {

    /**
     * Gets a [Flowable] that emits all exchange rates stored in this repository for the given
     * base currency.
     * Will continue emitting each time they change.
     */
    fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>>

    /**
     * Removes all existing exchange rates for given base stored in this repository and
     * inserts new ones.
     *
     * @param exchangeRates New exchange rates to be stored in this repository.
     * @param base Base currency for which the exchange rates are to be stored.
     */
    fun setExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency)

}
