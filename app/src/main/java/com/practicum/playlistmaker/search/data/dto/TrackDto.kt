package com.practicum.playlistmaker.search.data.dto

data class TrackDto( var trackName: String? = "Неизвестный трек",
                     var artistName: String? = "Неизвестный артист",
                     var trackTimeMillis: Int,
                     var artworkUrl100: String? = "",
                     var trackId: Int,
                     var collectionName: String? = "Неизвестный альбом",
                     var releaseDate: String? = "Дата неизвестна",
                     var primaryGenreName: String? = "Неизвестный жанр",
                     var country: String? = "Неизвестная страна",
                     var previewUrl: String? = "")

