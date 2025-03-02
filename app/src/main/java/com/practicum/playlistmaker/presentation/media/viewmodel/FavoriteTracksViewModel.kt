package com.practicum.playlistmaker.presentation.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.media.FavouriteTrackIterator
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

    //LiveData
    private val _stateView = MutableLiveData<FavouriteTrackViewState>()
    fun observeState(): LiveData<FavouriteTrackViewState> = _stateView

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            _stateView.postValue(FavouriteTrackViewState.Empty)
        } else{
            _stateView.postValue(FavouriteTrackViewState.Content(tracks))
        }
    }
}