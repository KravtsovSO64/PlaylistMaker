package com.practicum.playlistmaker.searchMusic.data.repositories.mapper

import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto
import com.practicum.playlistmaker.searchMusic.domain.models.Track

class MapperTrackDtoFromTrack {

    fun execute(track: Track): TrackDto{
        return TrackDto(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

}