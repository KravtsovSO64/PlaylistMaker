package com.practicum.playlistmaker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImagePath: String = "",
    var trackIdsJson: String = "[]",
    val trackCount: Int = 0
)