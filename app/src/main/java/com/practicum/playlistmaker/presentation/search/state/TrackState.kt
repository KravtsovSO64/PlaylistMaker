package com.practicum.playlistmaker.presentation.search.state

import com.practicum.playlistmaker.domain.model.Track

sealed class TrackState {
    data object Loading : TrackState()
    data class Error(val message: String) : TrackState()
    data class Content(val tracks: List<Track>) : TrackState()
    data class History(val tracks: List<Track>) : TrackState()
    data object Empty : TrackState()
}