package com.practicum.playlistmaker.domain.model

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImagePath: String = "",
    val trackIdsJson: String = "[]",
    val trackCount: Int = 0
)
