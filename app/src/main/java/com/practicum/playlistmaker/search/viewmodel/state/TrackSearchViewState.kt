package com.practicum.playlistmaker.search.viewmodel.state

import com.practicum.playlistmaker.search.domain.models.Track

sealed class TrackSearchViewState {
    data object Loading : TrackSearchViewState()
    data class Error(val code: Int) : TrackSearchViewState()
    data class Content(val tracks: List<Track>, val code: Int) : TrackSearchViewState()
    data class History(val tracks: List<Track>) : TrackSearchViewState()
}