package com.thedeekay.networking

import com.thedeekay.commons.Outcome
import com.thedeekay.commons.Outcome.Failure
import com.thedeekay.networking.NetworkFailure.Generic.*
import io.reactivex.Single
import java.net.SocketTimeoutException

/**
 * Decorator for [NetworkRequest] that handles generic errors and wraps specific ones.
 */
class ErrorWrapperNetworkRequest<T, in P, E>(
    private val connectivityChecker: ConnectivityChecker,
    private val wrappedRequest: NetworkRequest<T, P, E>
) : NetworkRequest<T, P, E> {

    override fun execute(params: P): Single<Outcome<T, NetworkFailure<E>>> {
        return Single.fromCallable { connectivityChecker.hasConnectivity() }
            .flatMap { hasConnectivity ->
                if (hasConnectivity.not()) Single.just(Failure(NoInternet))
                else wrappedRequest.execute(params)
                    .onErrorReturn { error ->
                        if (error is SocketTimeoutException) Failure(Timeout)
                        else Failure(Unknown)
                    }
            }
    }
}
