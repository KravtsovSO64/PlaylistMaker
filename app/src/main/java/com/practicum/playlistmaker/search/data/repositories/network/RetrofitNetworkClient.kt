package com.practicum.playlistmaker.search.data.repositories.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val musicApiService: MusicApiService,
    private val context: Context
): NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected(context)) Response().apply { resultCode = -1 }

        if (dto !is TrackSearchRequest) Response().apply { resultCode = 400 }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto){
                    is TrackSearchRequest -> musicApiService.searchMusic(dto.expression)
                    else -> TODO()
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
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