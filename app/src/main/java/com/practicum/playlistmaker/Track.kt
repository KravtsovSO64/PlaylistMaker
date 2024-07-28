package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(val trackName: String,
                 val artistName: String,
                 val trackTimeMillis: Long,
                 val artworkUrl100: String,
                 val trackId: Int,
                 val collectionName: String,
                 val releaseDate: String,
                 val primaryGenreName: String,
                 val country: String){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun getTrackTime(): String {
        val formatTime = SimpleDateFormat("mm:ss", Locale.getDefault())
        return formatTime.format(trackTimeMillis).toString()
    }
}

