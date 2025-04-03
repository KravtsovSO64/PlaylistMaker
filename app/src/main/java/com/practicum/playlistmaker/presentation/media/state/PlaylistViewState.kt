package com.practicum.playlistmaker.presentation.media.state

import com.practicum.playlistmaker.domain.model.Playlist

sealed interface PlaylistViewState {

    data class Content(val playlist: List<Playlist>, val enableErrorMessage: Boolean) : PlaylistViewState

    data class Empty(val enableErrorMessage: Boolean) : PlaylistViewState
}