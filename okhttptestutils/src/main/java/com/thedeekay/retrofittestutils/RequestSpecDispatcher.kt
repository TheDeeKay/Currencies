package com.thedeekay.retrofittestutils

import okhttp3.Headers
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * A dispatcher that enables specification of a matching request, along with a response for the
 * given request.
 *
 * Any requests not matching the given specification will throw an exception.
 */
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

// exists simply to enable 'forRequest {...}.respondWith {...}' syntax
fun forRequest(block: RequestSpecBuilder.() -> Unit): RequestSpec =
    requestSpec(block)

fun RequestSpec.respondWith(response: MockResponse.(RecordedRequest) -> Unit): RequestSpecDispatcher {
    val responseCreator = { request: RecordedRequest ->
        val mockResponse = MockResponse()
        mockResponse.response(request)
        mockResponse
    }
    return RequestSpecDispatcher(
        this,
        responseCreator
    )
}
