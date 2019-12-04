package com.thedeekay.exchangerates

import com.squareup.moshi.Json
import com.thedeekay.commons.Outcome
import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.Currency
import com.thedeekay.domain.ExchangeRate
import com.thedeekay.domain.div
import com.thedeekay.exchangerates.ExchangeRatesFailure.InvalidBase
import com.thedeekay.networking.NetworkFailure
import com.thedeekay.networking.NetworkFailure.Generic.Unknown
import com.thedeekay.networking.NetworkFailure.Specific
import com.thedeekay.networking.NetworkRequest
import io.reactivex.Single
import java.util.*

/**
 * Network request that fetches exchange rates between given base currency and all other available
 * currencies.
 */
class ExchangeRatesNetworkRequest internal constructor(
    private val exchangeRatesService: ExchangeRatesService
) : NetworkRequest<List<ExchangeRate>, ExchangeRatesRequestParams, ExchangeRatesFailure> {

    override fun execute(params: ExchangeRatesRequestParams): Single<Outcome<List<ExchangeRate>, NetworkFailure<ExchangeRatesFailure>>> {
        return exchangeRatesService.fetchAllExchangeRates(params.base.currencyCode)
            .map { response ->
                when {
                    response.isSuccessful -> {
                        val rates = response.body()!!
                        val base = params.base
                        Success(
                            rates.currencyAndRateMap.map { (counter, rate) ->
                                base / Currency(counter) at rate
                            }
                        )
                    }
                    response.code() == 422 -> Failure(Specific(InvalidBase(params.base)))
                    else -> Failure(Unknown)
                }
            }
    }
}

data class ExchangeRatesRequestParams(val base: Currency)

data class ExchangeRatesResponse(
    @Json(name = "rates")
    val currencyAndRateMap: Map<String, Double>
)

sealed class ExchangeRatesFailure {

    /**
     * Indicates that the server deemed the given currency as an invalid base currency.
     */
    data class InvalidBase(val base: Currency) : ExchangeRatesFailure()
}
