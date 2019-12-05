package com.thedeekay.networking

import com.thedeekay.commons.Outcome
import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.networking.NetworkFailure.Generic.*
import com.thedeekay.networking.NetworkFailure.Specific
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

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

    @Test
    fun `NoInternet failure should not be wrapped into Specific`() {
        val noInternet = Failure(NoInternet)
        wrappedRequest.outcome = { noInternet }

        wrapper.execute(Any()).test()

            .assertResult(noInternet)
    }

    @Test
    fun `Timeout failure should not be wrapped into Specific`() {
        val timeout = Failure(Timeout)
        wrappedRequest.outcome = { timeout }

        wrapper.execute(Any()).test()

            .assertResult(timeout)
    }

    @Test
    fun `Unknown failure should not be wrapped into Specific`() {
        val unknown = Failure(Unknown)
        wrappedRequest.outcome = { unknown }

        wrapper.execute(Any()).test()

            .assertResult(unknown)
    }

    @Test
    fun `SocketTimeoutException is mapped to timeout`() {
        wrappedRequest.outcome = { throw SocketTimeoutException() }

        wrapper.execute(Any()).test()

            .assertResult(Failure(Timeout))
    }

    @Test
    fun `IO exceptions are caught as Unknown`() {
        val ioException = IOException()
        wrappedRequest.outcome = { throw ioException }

        wrapper.execute(Any()).test()

            .assertResult(Failure(Unknown))
    }
}
