package com.thedeekay.networking

import com.thedeekay.commons.Outcome
import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.networking.NetworkFailure.Generic.NoInternet
import com.thedeekay.networking.NetworkFailure.Generic.Unknown
import com.thedeekay.networking.NetworkFailure.Specific
import org.junit.Before
import org.junit.Test

class ErrorWrapperNetworkRequestTest {

    private lateinit var connectivityChecker: FakeConnectivityChecker
    private lateinit var wrappedRequest: FakeNetworkRequest<Any, Any, Any>
    private lateinit var wrapper: ErrorWrapperNetworkRequest<Any, Any, Any>

    @Before
    fun setUp() {
        connectivityChecker = FakeConnectivityChecker()
        wrappedRequest = FakeNetworkRequest { Failure(Unknown) }

        wrapper = ErrorWrapperNetworkRequest(connectivityChecker, wrappedRequest)
    }

    @Test
    fun `no network should result in NoInternet failure`() {
        connectivityChecker.hasInternet = false

        wrapper.execute(Any()).test()

            .assertResult(Failure(NoInternet))
    }

    @Test
    fun `unsuccessful request should return the error wrapped in Specific`() {
        val error = Specific(Any())
        wrappedRequest.outcome = { Failure(error) }

        wrapper.execute(Any()).test()

            .assertResult(Failure(error))
    }

    @Test
    fun `successful requests should return unchanged result`() {
        val success = Outcome.Success(Any())
        wrappedRequest.outcome = { success }

        wrapper.execute(Any()).test()

            .assertResult(success)
    }
}
