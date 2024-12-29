package com.practicum.playlistmaker.media.viewmodel.state

import com.practicum.playlistmaker.media.domain.models.Track

sealed interface PlaylistViewState {

    data class Content(val playList: List<Track>) : PlaylistViewState

    data class Error(val message: String): PlaylistViewState
}