package com.practicum.playlistmaker.presentation.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.media.favorite.FavouriteTrackIterator
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.state.FavouriteTrackViewState
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val iterator: FavouriteTrackIterator
) : ViewModel() {

    fun getListFavourite() {
        viewModelScope.launch {
            iterator
                .getListFavouriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private val _stateFavourite = MutableLiveData<FavouriteTrackViewState>()
    val stateFavourite: LiveData<FavouriteTrackViewState> get() = _stateFavourite

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            _stateFavourite.postValue(FavouriteTrackViewState.Empty)
        } else{
            _stateFavourite.postValue(FavouriteTrackViewState.Content(tracks))
        }
    }
}