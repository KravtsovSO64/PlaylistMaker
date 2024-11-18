package com.practicum.playlistmaker.search.domain.models

import java.io.Serializable

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String,
    val trackId: Int,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String,
    val previewUrl: String
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
}