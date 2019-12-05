package com.thedeekay.exchangerates

import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable

class DefaultExchangeRatesRepository : ExchangeRatesRepository {
    override fun allExchangeRates(): Flowable<List<ExchangeRate>> {
        return Flowable.just(emptyList<ExchangeRate>())
            .mergeWith(Flowable.never())
    }

    override fun setExchangeRates(exchangeRates: List<ExchangeRate>) {

    }
}
