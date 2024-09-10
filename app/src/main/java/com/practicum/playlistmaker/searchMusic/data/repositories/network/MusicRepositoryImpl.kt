package com.practicum.playlistmaker.searchMusic.data.repositories.network

import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicRepository

class MusicRepositoryImpl(private val networkClient: NetworkClient): MusicRepository {

    override fun searchMusic(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if (response.resultCode == 200){
            return (response as TrackSearchResponse).result.map {
                Track(it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl)
            }
        } else {
            return emptyList()
        }
    }

}