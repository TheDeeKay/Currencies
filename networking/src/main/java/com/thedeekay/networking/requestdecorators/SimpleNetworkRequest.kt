package com.thedeekay.networking.requestdecorators

import com.thedeekay.commons.Outcome
import com.thedeekay.networking.NetworkFailure
import com.thedeekay.networking.NetworkRequest
import io.reactivex.Single

/**
 * Simple [NetworkRequest] class for easy implementation by delegation.
 */
private class SimpleNetworkRequest<T, in P, E>(
    private val request: (P) -> Single<Outcome<T, NetworkFailure<E>>>
) : NetworkRequest<T, P, E> {
    override fun execute(params: P): Single<Outcome<T, NetworkFailure<E>>> = request(params)
}

fun <T, P, E> simpleRequest(
    request: (P) -> Single<Outcome<T, NetworkFailure<E>>>
): NetworkRequest<T, P, E> =
    SimpleNetworkRequest(request)
