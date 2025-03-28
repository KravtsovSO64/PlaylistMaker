package com.practicum.playlistmaker.data.db.converter

import com.practicum.playlistmaker.data.db.entities.PlaylistEntity
import com.practicum.playlistmaker.domain.model.Playlist


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
}