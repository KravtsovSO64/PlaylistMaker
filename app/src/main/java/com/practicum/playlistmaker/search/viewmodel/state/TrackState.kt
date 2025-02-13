package com.practicum.playlistmaker.search.viewmodel.state

import com.practicum.playlistmaker.search.domain.models.Track

sealed class TrackState {
    data object Loading : TrackState()
    data class Error(val message: String) : TrackState()
    data class Content(val tracks: List<Track>) : TrackState()
    data class History(val tracks: List<Track>) : TrackState()
    data object Empty : TrackState()
}