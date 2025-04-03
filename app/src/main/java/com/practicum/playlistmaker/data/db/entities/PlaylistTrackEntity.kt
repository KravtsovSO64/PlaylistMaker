package com.practicum.playlistmaker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track_table")
data class PlaylistTrackEntity(
    var trackName: String? = "Неизвестный трек",
    var artistName: String? = "Неизвестный артист",
    var trackTimeMillis: Int,
    var artworkUrl100: String? = "",
    @PrimaryKey(autoGenerate = false)
    var trackId: Int,
    var collectionName: String? = "Неизвестный альбом",
    var releaseDate: String? = "Дата неизвестна",
    var primaryGenreName: String? = "Неизвестный жанр",
    var country: String? = "Неизвестная страна",
    var previewUrl: String? = "",
    var timestamp: String = System.currentTimeMillis().toString()
)
