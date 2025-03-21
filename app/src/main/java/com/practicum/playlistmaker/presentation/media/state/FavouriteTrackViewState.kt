package com.practicum.playlistmaker.presentation.media.state

import com.practicum.playlistmaker.domain.model.Track

sealed interface FavouriteTrackViewState {

    data class Content(val favoriteList: List<Track>) : FavouriteTrackViewState

    data object Empty : FavouriteTrackViewState
}