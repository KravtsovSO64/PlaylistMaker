package com.practicum.playlistmaker.search.data.dto

data class TrackSearchResponse(val text: String,
                               val results: List<TrackDto>): Response()
