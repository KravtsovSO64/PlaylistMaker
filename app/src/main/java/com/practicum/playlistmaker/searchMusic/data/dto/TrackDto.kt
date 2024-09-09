package com.practicum.playlistmaker.searchMusic.data.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto (val wrapperType : String,
                     val kind : String,
                     val artistId : Int,
                     val collectionId : Int,
                     val trackId : Int,
                     val artistName : String,
                     val collectionName : String,
                     val trackName : String,
                     val collectionCensoredName : String,
                     val trackCensoredName : String,
                     val artistViewUrl : String,
                     val collectionViewUrl : String,
                     val trackViewUrl : String,
                     val previewUrl : String,
                     val artworkUrl30 : String,
                     val artworkUrl60 : String,
                     val artworkUrl100 : String,
                     val collectionPrice : Double,
                     val trackPrice : Double,
                     val releaseDate : String,
                     val collectionExplicitness : String,
                     val trackExplicitness : String,
                     val discCount : Int,
                     val discNumber : Int,
                     val trackCount : Int,
                     val trackNumber : Int,
                     val trackTimeMillis : Int,
                     val country : String,
                     val currency : String,
                     val primaryGenreName : String,
                     val contentAdvisoryRating : String,
                     val isStreamable : Boolean) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun getTrackTime(): String {
        val formatTime = SimpleDateFormat("mm:ss", Locale.getDefault())
        return formatTime.format(trackTimeMillis).toString()
    }
}


