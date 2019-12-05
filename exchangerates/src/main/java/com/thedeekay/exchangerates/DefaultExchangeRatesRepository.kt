package com.thedeekay.exchangerates

import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable
import java.util.*

class DefaultExchangeRatesRepository : ExchangeRatesRepository {

    override fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>> {
        return Flowable.just(emptyList<ExchangeRate>())
            .mergeWith(Flowable.never())
    }

    override fun setExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency) {

    }
}
