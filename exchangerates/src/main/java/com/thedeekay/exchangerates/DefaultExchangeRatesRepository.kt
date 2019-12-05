package com.thedeekay.exchangerates

import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

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
            .switchMapSingle { rates ->
                if (rates.isEmpty()) {
                    exchangeRatesNetworkRequest.execute(ExchangeRatesRequestParams(base))
                        .flatMap { outcome ->
                            if (outcome is Success) {
                                Single.never<List<ExchangeRate>>()
                                    .doOnSubscribe { setExchangeRates(outcome.result, base) }
                            } else {
                                Single.just(emptyList())
                            }
                        }
                } else {
                    Single.just(rates)
                }
            }
    }

    override fun setExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency) {
        exchangeRatesDao.insertExchangeRates(exchangeRates, base)
    }
}
