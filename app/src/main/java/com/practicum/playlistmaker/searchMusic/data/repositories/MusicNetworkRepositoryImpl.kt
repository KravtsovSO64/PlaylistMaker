package com.practicum.playlistmaker.searchMusic.data.repositories

import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.searchMusic.data.repositories.mapper.MapperTrackFromTrackDto
import com.practicum.playlistmaker.searchMusic.data.repositories.network.NetworkStorage
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicNetworkRepository

class MusicNetworkRepositoryImpl(private val networkStorage: NetworkStorage): MusicNetworkRepository {

    override fun searchMusic(expression: String): List<Track> {
        val response = networkStorage.doRequest(TrackSearchRequest(expression))

        return if (response.resultCode in 200..299) {
            val resultList = (response as TrackSearchResponse).results


            if (resultList != null) {
                resultList.map {
                    MapperTrackFromTrackDto().execute(it)
                }
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}

