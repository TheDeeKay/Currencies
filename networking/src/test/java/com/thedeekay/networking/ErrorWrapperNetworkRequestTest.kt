package com.thedeekay.networking

import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.networking.NetworkFailure.Generic.NoInternet
import com.thedeekay.networking.NetworkFailure.Generic.Unknown
import com.thedeekay.networking.NetworkFailure.Specific
import org.junit.Test

class ErrorWrapperNetworkRequestTest {

    @Test
    fun `no network should result in NoInternet failure`() {
        val connectivityChecker = FakeConnectivityChecker(hasInternet = false)
        val wrappedRequest = FakeNetworkRequest<Any, Any, Any> { Failure(Unknown) }
        val wrapper = ErrorWrapperNetworkRequest(connectivityChecker, wrappedRequest)

        wrapper.execute(Any()).test()

            .assertResult(Failure(NoInternet))
    }

    @Test
    fun `unsuccessful request should return the error wrapped in Specific`() {
        val error = Specific(Any())
        val connectivityChecker = FakeConnectivityChecker()
        val wrappedRequest = FakeNetworkRequest<Any, Any, Any> { Failure(error) }
        val wrapper = ErrorWrapperNetworkRequest(connectivityChecker, wrappedRequest)
        wrappedRequest.outcome = { Failure(error) }

        wrapper.execute(Any()).test()

            .assertResult(Failure(error))
    }
}
