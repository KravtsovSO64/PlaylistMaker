package com.practicum.playlistmaker.data.dto.search

data class TrackSearchResponse(val text: String,
                               val results: List<TrackDto>): Response()
