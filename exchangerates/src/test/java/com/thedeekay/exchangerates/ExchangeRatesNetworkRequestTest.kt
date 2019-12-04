package com.thedeekay.exchangerates

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.*
import com.thedeekay.retrofittestutils.respondWith
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

// TODO: add RxJava rule for schedulers
// TODO: do something with potentially uncaught RxJava exceptions
class ExchangeRatesNetworkRequestTest {

    private lateinit var server: MockWebServer
    private lateinit var request: ExchangeRatesNetworkRequest

    @Before
    fun setUp() {
        server = MockWebServer()

        val retrofit = createRetrofit(baseUrl = server.url(""))
        val exchangeRatesService = retrofit.create(ExchangeRatesService::class.java)
        request = ExchangeRatesNetworkRequest(exchangeRatesService)
    }

    @Test
    fun `exchange rates network request should hit proper endpoint and parse rates`() {
        val exchangeRatesResponse =
            """
            {
                "base":"EUR",
                "date":"2018-09-06",
                "rates":{
                    "CHF":1.1309,
                    "GBP":0.90094,
                    "JPY":129.94,
                    "RUB":79.814,
                    "USD":1.1669
                }
            }
            """
        val expectedExchangeRates = listOf(
            EUR / CHF at 1.1309,
            EUR / GBP at 0.90094,
            EUR / JPY at 129.94,
            EUR / RUB at 79.814,
            EUR / USD at 1.1669
        )
        server.dispatcher =
            com.thedeekay.retrofittestutils.forRequest {
                path = "/latest?base=EUR"
                method = "GET"
            }.respondWith {
                setResponseCode(200)
                setBody(exchangeRatesResponse)
            }

        request.execute(ExchangeRatesRequestParams(EUR)).test()

            .assertValue(Success(expectedExchangeRates))
            .assertComplete()
    }

    private fun createRetrofit(baseUrl: HttpUrl): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }
}
