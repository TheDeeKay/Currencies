package com.thedeekay.exchangerates.network

import com.thedeekay.exchangerates.network.ExchangeRatesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service for communicating with web services related to exchange rates.
 */
internal interface ExchangeRatesService {

    @GET("/latest")
    fun fetchAllExchangeRates(
        @Query("base") baseCurrency: String
    ): Single<Response<ExchangeRatesResponse>>

}
