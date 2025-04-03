package com.practicum.playlistmaker.data.db.converter

import com.practicum.playlistmaker.data.db.entities.PlaylistEntity
import com.practicum.playlistmaker.data.db.entities.PlaylistTrackEntity
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track


class PlaylistDbConverter() {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name =playlist.name,
            description =playlist.description,
            coverImagePath =playlist.coverImagePath,
            trackIdsJson =playlist.trackIdsJson,
            trackCount =playlist.trackCount,
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            name =playlistEntity.name,
            description =playlistEntity.description,
            coverImagePath =playlistEntity.coverImagePath,
            trackIdsJson =playlistEntity.trackIdsJson,
            trackCount =playlistEntity.trackCount,
        )
    }

    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
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

    fun map(track: PlaylistTrackEntity): Track {
        return Track(
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
}