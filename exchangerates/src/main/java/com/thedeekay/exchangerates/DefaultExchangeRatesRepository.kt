package com.thedeekay.exchangerates

import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.ExchangeRate
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

    private val exchangeRatesDao = exchangeRatesDatabase.exchangeRates()

    override fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>> {
        return exchangeRatesDao.allExchangeRates(base)
            .mergeWith(
                exchangeRatesNetworkRequest.execute(ExchangeRatesRequestParams(base))
                    .doOnSuccess { if (it is Success) setExchangeRates(it.result, base) }
                    .ignoreElement()
            )
            .mergeWith(
                Flowable.interval(1, SECONDS)
                    .flatMapCompletable {
                        exchangeRatesNetworkRequest.execute(ExchangeRatesRequestParams(base))
                            .doOnSuccess { if (it is Success) setExchangeRates(it.result, base) }
                            .ignoreElement()
                    }
            )
    }

    override fun setExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency) {
        exchangeRatesDao.insertExchangeRates(exchangeRates, base)
    }
}
