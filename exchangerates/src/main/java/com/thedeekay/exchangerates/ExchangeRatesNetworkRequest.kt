package com.thedeekay.exchangerates

import com.thedeekay.commons.Outcome
import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.ExchangeRate
import com.thedeekay.networking.NetworkFailure
import com.thedeekay.networking.NetworkRequest
import io.reactivex.Single
import java.util.*

/**
 * Network request that fetches exchange rates between given base currency and all other available
 * currencies.
 */
// TODO: consider adding 'invalid base currency' error type
class ExchangeRatesNetworkRequest :
    NetworkRequest<List<ExchangeRate>, ExchangeRatesRequestParams, Nothing> {

    override fun execute(params: ExchangeRatesRequestParams): Single<Outcome<List<ExchangeRate>, NetworkFailure<Nothing>>> {
        return Single.just(Success(listOf()))
    }
}

data class ExchangeRatesRequestParams(
    val base: Currency
)
