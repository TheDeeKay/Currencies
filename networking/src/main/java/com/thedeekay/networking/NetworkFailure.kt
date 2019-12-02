package com.thedeekay.networking

/**
 * Sealed class containing failure reason of a network request.
 *
 * Subtypes of this can be either generic network errors, embodied by [Generic], or errors specific
 * to some given request, embodied by [Specific].
 *
 * @param E Type of specific error that this can contain.
 */
sealed class NetworkFailure<out E> {

    sealed class Generic : NetworkFailure<Nothing>() {
        object NoInternet : Generic()
        object Timeout : Generic()
    }

    data class Specific<out E>(val failure: E) : NetworkFailure<E>()
}

typealias PureNetworkFailure = NetworkFailure<Nothing>
