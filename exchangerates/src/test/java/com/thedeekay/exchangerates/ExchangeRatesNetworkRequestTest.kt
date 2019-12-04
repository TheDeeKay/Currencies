package com.thedeekay.exchangerates

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.*
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

// TODO: add RxJava rule for schedulers
// TODO: do something with potentially uncaught RxJava exceptions
class ExchangeRatesNetworkRequestTest {

    private lateinit var server: MockWebServer
    private lateinit var retrofit: Retrofit

    @Before
    fun setUp() {
        server = MockWebServer()
        retrofit = createRetrofit(baseUrl = server.url(""))
    }

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
        server.dispatcher =
            forRequest {
                path = "/latest?base=EUR"
                method = "GET"
            }.respondWith {
                setResponseCode(200)
                setBody(exchangeRatesResponse)
            }
        val exchangeRatesService = retrofit.create(ExchangeRatesService::class.java)

        val request = ExchangeRatesNetworkRequest(exchangeRatesService)

        request.execute(ExchangeRatesRequestParams(EUR))
            .test()
            .assertValue(Success(expectedExchangeRates))
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

class RequestSpecDispatcher(
    private val requestSpec: RequestSpec,
    private val responseCreator: (RecordedRequest) -> MockResponse
) : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse = with(requestSpec) {
        if (
            (headers == null || headers == request.headers)
            && (method == null || method == request.method)
            && (headers == null || headers == request.headers)
        ) {
            return@with responseCreator(request)
        } else {
            throw IllegalArgumentException("Request: $request did not match the given spec: $requestSpec")
        }
    }
}

data class RequestSpec(
    val path: String? = null,
    val method: String? = null,
    val headers: Headers? = null
)

class RequestSpecBuilder(
    var path: String? = null,
    var method: String? = null,
    var headers: Headers? = null
)

fun requestSpec(block: RequestSpecBuilder.() -> Unit): RequestSpec =
    with(RequestSpecBuilder().apply(block)) {
        RequestSpec(path, method, headers)
    }

fun forRequest(block: RequestSpecBuilder.() -> Unit): RequestSpec = requestSpec(block)

fun RequestSpec.respondWith(response: MockResponse.(RecordedRequest) -> Unit): RequestSpecDispatcher {
    val responseCreator = { request: RecordedRequest ->
        val mockResponse = MockResponse()
        mockResponse.response(request)
        mockResponse
    }
    return RequestSpecDispatcher(this, responseCreator)
}
