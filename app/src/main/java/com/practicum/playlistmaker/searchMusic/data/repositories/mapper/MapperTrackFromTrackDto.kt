package com.practicum.playlistmaker.searchMusic.data.repositories.mapper

import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto
import com.practicum.playlistmaker.searchMusic.domain.models.Track

class MapperTrackFromTrackDto {

    fun execute(trackDto: TrackDto): Track {
        return Track(
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl100 = trackDto.artworkUrl100,
            trackId = trackDto.trackId,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
        )
    }
}