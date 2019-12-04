package com.thedeekay.exchangerates

import com.thedeekay.commons.Outcome.Success
import com.thedeekay.domain.EUR
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test

class ExchangeRatesNetworkRequestTest {

    @Test
    fun `exchange rates network request should hit proper endpoint and parse rates`() {
        val response = ""
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path == "latest?base=EUR" && request.method?.toUpperCase() == "GET") {
                    return MockResponse().apply {
                        setResponseCode(200)
                        setBody(response)
                    }
                } else {
                    throw IllegalArgumentException("Wrong request path or method!")
                }
            }

        }

        val request = ExchangeRatesNetworkRequest()

        request.execute(ExchangeRatesRequestParams(EUR))
            .test()
            .assertValue(Success(listOf()))
    }
}
