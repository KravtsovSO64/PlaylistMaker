package com.practicum.playlistmaker.searchMusic.data.repositories

import android.util.Log
import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.searchMusic.data.repositories.mapper.MapperTrackFromTrackDto
import com.practicum.playlistmaker.searchMusic.data.repositories.network.NetworkStorage
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicNetworkRepository

class MusicNetworkRepositoryImpl(private val networkStorage: NetworkStorage): MusicNetworkRepository {

    override fun searchMusic(expression: String): List<Track> {
        val response = networkStorage.doRequest(TrackSearchRequest(expression))

        Log.d("Error 1", "Response received: $response") // Логируем ответ

        return if (response.resultCode in 200..299) {
            val resultList = (response as TrackSearchResponse).result


            if (resultList != null) {
                resultList.map {
                    MapperTrackFromTrackDto().execute(it)
                }
            } else {
                Log.e("Error 2", "Result is null") // Логируем ошибку
                emptyList()
            }
        } else {
            Log.e("Error 3", "Error code: ${response.resultCode}") // Логируем код ошибки
            emptyList()
        }
    }
}

