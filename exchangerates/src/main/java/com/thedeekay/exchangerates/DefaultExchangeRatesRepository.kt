package com.thedeekay.exchangerates

import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable
import java.util.*

class DefaultExchangeRatesRepository(
    exchangeRatesDatabase: ExchangeRatesDatabase
) : ExchangeRatesRepository {

    private val exchangeRatesDao = exchangeRatesDatabase.exchangeRates()

    override fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>> {
        return exchangeRatesDao.allExchangeRates(base).map { rates -> rates.map { ExchangeRate(it.base, it.counter, it.rate) } }
    }

    override fun setExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency) {
        exchangeRatesDao.insertExchangeRates(exchangeRates.map { ExchangeRateEntity(it.base, it.counter, it.rate) })
    }
}
