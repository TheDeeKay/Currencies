package com.thedeekay.exchangerates

import com.squareup.moshi.Json
import com.thedeekay.commons.Outcome
import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.Currency
import com.thedeekay.domain.ExchangeRate
import com.thedeekay.domain.div
import com.thedeekay.networking.NetworkFailure
import com.thedeekay.networking.NetworkRequest
import io.reactivex.Single
import java.util.*

/**
 * Network request that fetches exchange rates between given base currency and all other available
 * currencies.
 */
// TODO: consider adding 'invalid base currency' error type
class ExchangeRatesNetworkRequest internal constructor(
    private val exchangeRatesService: ExchangeRatesService
) : NetworkRequest<List<ExchangeRate>, ExchangeRatesRequestParams, Nothing> {

    override fun execute(params: ExchangeRatesRequestParams): Single<Outcome<List<ExchangeRate>, NetworkFailure<Nothing>>> {
        return exchangeRatesService.fetchAllExchangeRates(params.base.currencyCode)
            .map { response ->
                if (response.isSuccessful) {
                    val rates = response.body()!!
                    val base = params.base
                    Success(
                        rates.currencyAndRateMap.map { (counter, rate) ->
                            base / Currency(counter) at rate
                        }
                    )
                } else TODO("handle this as well")
            }
    }
}

data class ExchangeRatesRequestParams(val base: Currency)

data class ExchangeRatesResponse(
    @Json(name = "rates")
    val currencyAndRateMap: Map<String, Double>
)
