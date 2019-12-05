package com.thedeekay.networking

import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.networking.NetworkFailure.Generic.NoInternet
import com.thedeekay.networking.NetworkFailure.Generic.Unknown
import org.junit.Test

class ErrorWrapperNetworkRequestTest {

    @Test
    fun `no network should result in NoInternet failure`() {
        val connectivityChecker = FakeConnectivityChecker()
        val wrappedRequest = FakeNetworkRequest<Any, Any, Any> { Failure(Unknown) }
        val wrapper = ErrorWrapperNetworkRequest(connectivityChecker, wrappedRequest)

        wrapper.execute(Any()).test()

            .assertValue(Failure(NoInternet))
    }
}
