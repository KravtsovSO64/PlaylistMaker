package com.practicum.playlistmaker.presentation.search.state

import com.practicum.playlistmaker.domain.model.Track

sealed class State {
    data object Loading: State()
    data class Error(val message: String): State()
    data class Content(val tracks: List<Track>): State()
    data class History(val tracks: List<Track>): State()
    data object Empty: State()
    data object Default: State()
}