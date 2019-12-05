package com.thedeekay.networking

import com.thedeekay.commons.Outcome
import io.reactivex.Single
import io.reactivex.Single.fromCallable

/**
 * A specification for network request, triggering one when [execute] is called.
 *
 * @param T Type of successful result returned by this request.
 * @param P Type of parameters required to make a request.
 * @param E Type of failure returned by this request.
 */
interface NetworkRequest<T, in P, E> {

    /**
     * Executes the network request encapsulated by this object and returns its [Outcome].
     *
     * @param params Parameters for this request.
     */
    fun execute(params: P): Single<Outcome<T, NetworkFailure<E>>>
}

class FakeNetworkRequest<T, P, E>(
    var outcome: (P) -> Outcome<T, NetworkFailure<E>>
) : NetworkRequest<T, P, E> {
    override fun execute(params: P): Single<Outcome<T, NetworkFailure<E>>> =
        fromCallable { outcome(params) }
}
