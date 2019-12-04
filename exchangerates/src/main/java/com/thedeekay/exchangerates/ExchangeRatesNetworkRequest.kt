package com.thedeekay.exchangerates

import com.thedeekay.commons.Outcome
import com.thedeekay.domain.ExchangeRate
import com.thedeekay.networking.NetworkFailure
import com.thedeekay.networking.NetworkRequest
import io.reactivex.Single
import java.util.*

/**
 * Network request that fetches exchange rates between given base currency and all other available
 * currencies.
 */
// TODO: add 'invalid base currency' error type
class ExchangeRatesNetworkRequest :
    NetworkRequest<List<ExchangeRate>, ExchangeRatesRequestParams, Nothing> {

    override fun execute(params: ExchangeRatesRequestParams): Single<Outcome<List<ExchangeRate>, NetworkFailure<Nothing>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class ExchangeRatesRequestParams(
    val base: Currency
)
