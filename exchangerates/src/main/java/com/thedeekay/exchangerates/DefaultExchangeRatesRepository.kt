package com.thedeekay.exchangerates

import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable
import java.util.*

/**
 * Default implementation of [ExchangeRatesRepository] using Room.
 */
class DefaultExchangeRatesRepository(
    exchangeRatesDatabase: ExchangeRatesDatabase,
    exchangeRatesNetworkRequest: ExchangeRatesNetworkRequest
) : ExchangeRatesRepository {

    private val exchangeRatesDao = exchangeRatesDatabase.exchangeRates()

    override fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>> {
        return exchangeRatesDao.allExchangeRates(base)
    }

    override fun setExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency) {
        exchangeRatesDao.insertExchangeRates(exchangeRates, base)
    }
}
