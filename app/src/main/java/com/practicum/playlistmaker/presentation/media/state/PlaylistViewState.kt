package com.practicum.playlistmaker.presentation.media.state

import com.practicum.playlistmaker.domain.model.Track

sealed interface PlaylistViewState {

    data class Content(val playList: List<Track>) : PlaylistViewState

    data class Error(val message: String): PlaylistViewState
}