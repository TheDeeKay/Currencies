package com.thedeekay.networking

import com.thedeekay.commons.Outcome
import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.networking.NetworkFailure.Generic.NoInternet
import io.reactivex.Single

/**
 * Decorator for [NetworkRequest] that handles generic errors and wraps specific ones.
 */
class ErrorWrapperNetworkRequest<T, in P, E>(
    connectivityChecker: ConnectivityChecker,
    wrappedRequest: NetworkRequest<T, P, E>
) : NetworkRequest<T, P, E> {

    override fun execute(params: P): Single<Outcome<T, NetworkFailure<E>>> {
        return Single.just(Failure(NoInternet))
    }
}
