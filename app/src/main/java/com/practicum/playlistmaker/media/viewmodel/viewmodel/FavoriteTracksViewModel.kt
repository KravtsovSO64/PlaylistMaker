package com.practicum.playlistmaker.media.viewmodel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.viewmodel.state.FavoriteTrackViewState

class FavoriteTracksViewModel() : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteTrackViewState>()
    fun observeState(): LiveData<FavoriteTrackViewState> = stateLiveData

}