package com.thedeekay.exchangerates

import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.ExchangeRate
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

/**
 * Default implementation of [ExchangeRatesRepository] using Room.
 */
class DefaultExchangeRatesRepository(
    exchangeRatesDatabase: ExchangeRatesDatabase,
    private val exchangeRatesNetworkRequest: ExchangeRatesNetworkRequest
) : ExchangeRatesRepository {

    private val allExchangeRatesMap = mutableMapOf<Currency, Flowable<List<ExchangeRate>>>()

    private val exchangeRatesDao = exchangeRatesDatabase.exchangeRates()

    @Synchronized
    override fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>> {
        return allExchangeRatesMap.getOrPut(base) {
            exchangeRatesDao.allExchangeRates(base)
                .mergeWith(
                    Flowable.interval(0, 1, SECONDS)
                        .flatMapCompletable { fetchRates(base) }
                )
                .share()
        }
    }

    override fun setExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency) {
        exchangeRatesDao.insertExchangeRates(exchangeRates, base)
    }

    private fun fetchRates(base: Currency): Completable {
        return exchangeRatesNetworkRequest.execute(ExchangeRatesRequestParams(base))
            .doOnSuccess { if (it is Success) setExchangeRates(it.result, base) }
            .ignoreElement()
    }

}
