package com.thedeekay.networking.connectivity

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * Default implementation of [ConnectivityChecker], based on Android's system services.
 */
class DefaultConnectivityChecker @Inject constructor(
    context: Context
) : ConnectivityChecker {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Suppress("DEPRECATION")
    override fun hasConnectivity(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}
