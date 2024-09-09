package com.practicum.playlistmaker.searchMusic.data.dto

data class TrackSearchResponse(val searchType: String,
                               val expression: String,
                               val result: List<TrackDto>): Response()
