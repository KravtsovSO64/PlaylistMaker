package com.practicum.playlistmaker.searchMusic.data.dto

data class TrackSearchResponse(val expression: String,
                               val result: List<TrackDto>): Response()
