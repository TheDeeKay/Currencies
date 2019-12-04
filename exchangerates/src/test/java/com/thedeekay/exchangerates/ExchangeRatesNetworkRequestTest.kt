package com.thedeekay.exchangerates

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class ExchangeRatesNetworkRequestTest {

    @Test
    fun `exchange rates network request should hit proper endpoint and parse rates`() {
        val exchangeRatesResponse =
            """{
                "base":"EUR",
                "date":"2018-09-06",
                "rates":{
                    "CHF":1.1309,
                    "GBP":0.90094,
                    "JPY":129.94,
                    "RUB":79.814,
                    "USD":1.1669
                }
            }"""
        val expectedExchangeRates = listOf(
            EUR / CHF at 1.1309,
            EUR / GBP at 0.90094,
            EUR / JPY at 129.94,
            EUR / RUB at 79.814,
            EUR / USD at 1.1669
        )
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path == "/latest?base=EUR" && request.method?.toUpperCase() == "GET") {
                    return MockResponse().apply {
                        setResponseCode(200)
                        setBody(exchangeRatesResponse)
                    }
                } else {
                    throw IllegalArgumentException("Wrong request path or method!")
                }
            }

        }
        val retrofit = Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(server.url(""))
            .build()
        val exchangeRatesService = retrofit.create(ExchangeRatesService::class.java)

        val request = ExchangeRatesNetworkRequest(exchangeRatesService)

        request.execute(ExchangeRatesRequestParams(EUR))
            .test()
            .assertValue(Success(expectedExchangeRates))
    }
}
