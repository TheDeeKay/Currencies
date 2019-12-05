package com.thedeekay.networking.connectivity

/**
 * Checker for whether there is an internet connection or not at the time.
 */
interface ConnectivityChecker {

    /**
     * Checks whether there is currently a connection to the internet.
     */
    fun hasConnectivity(): Boolean
}

class FakeConnectivityChecker(
    var hasInternet: Boolean = true
) : ConnectivityChecker {
    override fun hasConnectivity(): Boolean = hasInternet
}
