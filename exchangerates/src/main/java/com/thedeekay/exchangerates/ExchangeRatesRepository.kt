package com.thedeekay.exchangerates

import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable

/**
 * Repository that manipulates storage and fetching of exchange rates.
 */
interface ExchangeRatesRepository {

    /**
     * Gets a [Flowable] that emits all exchange rates stored in this repository each time
     * they change.
     */
    fun allExchangeRates(): Flowable<List<ExchangeRate>>

    /**
     * Removes all existing exchange rates stored in this repository and inserts new ones.
     *
     * @param exchangeRates New exchange rates to be stored in this repository.
     */
    fun setExchangeRates(exchangeRates: List<ExchangeRate>)

}
