package com.practicum.playlistmaker.data.db.converter

import com.practicum.playlistmaker.data.db.entities.FavouriteTrackEntity
import com.practicum.playlistmaker.domain.model.Track

class TrackDbConverter {


    fun map(track: Track): FavouriteTrackEntity {
        return FavouriteTrackEntity(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: FavouriteTrackEntity): Track {
        return Track(
            track.trackName.toString(),
            track.artistName.toString(),
            track.trackTimeMillis,
            track.artworkUrl100.toString(),
            track.trackId,
            track.collectionName.toString(),
            track.releaseDate.toString(),
            track.primaryGenreName.toString(),
            track.country.toString(),
            track.previewUrl.toString(),
            isFavorite = true
        )

    }
}

