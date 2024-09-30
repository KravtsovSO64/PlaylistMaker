package com.practicum.playlistmaker.searchMusic.data.dto

data class TrackSearchResponse(val text: String,
                               val results: List<TrackDto>): Response()
