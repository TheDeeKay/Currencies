package com.thedeekay.networking.requestdecorators

import com.thedeekay.commons.Outcome
import com.thedeekay.networking.NetworkFailure
import com.thedeekay.networking.NetworkRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Default implementation for [NetworkRequest]. Includes threading and other default settings.
 */
fun <T, P, E> defaultNetworkRequest(
    request: (P) -> Single<Outcome<T, NetworkFailure<E>>>
): NetworkRequest<T, P, E> =
    simpleRequest { params ->
        request(params)
            .subscribeOn(Schedulers.io())
    }
