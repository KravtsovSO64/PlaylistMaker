package com.practicum.playlistmaker.search.presentation.state

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface TrackSearchState {
    data object Loading: TrackSearchState
    data class Error(val code: Int): TrackSearchState
    data class Content(val data: List<Track>, val code: Int): TrackSearchState
}