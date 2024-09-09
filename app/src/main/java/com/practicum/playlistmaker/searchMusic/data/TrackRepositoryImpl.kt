package com.practicum.playlistmaker.searchMusic.data

import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.searchMusic.domain.api.MusicRepository
import com.practicum.playlistmaker.searchMusic.domain.entities.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient): MusicRepository {
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