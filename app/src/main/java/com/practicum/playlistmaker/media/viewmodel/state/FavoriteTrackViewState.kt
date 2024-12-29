package com.practicum.playlistmaker.media.viewmodel.state

import com.practicum.playlistmaker.media.domain.models.Track

sealed interface FavoriteTrackViewState {

    data class Content(val favoriteList: List<Track>) : FavoriteTrackViewState

    data class Error(val message: String): FavoriteTrackViewState
}