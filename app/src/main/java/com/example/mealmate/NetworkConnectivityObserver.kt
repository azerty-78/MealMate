package com.example.mealmate

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NetworkConnectivityObserver
@Inject constructor(
    private val context: Context
): ConnectivityObserver{
    private val connectityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    companion object{ private const val TAG = "NetworkConnectivityObserver" }

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(ConnectivityObserver.Status.Available)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(ConnectivityObserver.Status.Unavailable)
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val isWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    val isCellular = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    if (isWifi || isCellular) {
                        trySend(ConnectivityObserver.Status.Available)
                    }else{
                        trySend(ConnectivityObserver.Status.Unavailable)
                    }
                }
            }

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectityManager.registerNetworkCallback(request, callback)

            awaitClose {
                connectityManager.unregisterNetworkCallback(callback)
            }
        }
    }

    override fun isOnline(): Boolean {
        val network = connectityManager.activeNetwork ?: return false
        val capabilities = connectityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}


interface ConnectivityObserver {
    fun observe(): Flow<Status>
    fun isOnline(): Boolean
    enum class Status{
        Available, Unavailable, Neutral
    }

}