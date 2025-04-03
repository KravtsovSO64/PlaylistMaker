package com.practicum.playlistmaker.domain.model

import java.io.Serializable

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImagePath: String = "",
    val trackIdsJson: String = "[]",
    val trackCount: Int = 0
): Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
