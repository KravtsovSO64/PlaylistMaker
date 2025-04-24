package com.practicum.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkBroadcastReceiver: BroadcastReceiver() {

    private companion object {
        const val NETWORK_STATE = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == NETWORK_STATE && context != null && !isConnected(context)) {
            Toast.makeText(context, "Нет подключения к интернету", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}