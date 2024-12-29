package com.practicum.playlistmaker.media.viewmodel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.viewmodel.state.PlaylistViewState

class PlaylistViewModel() : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistViewState>()
    fun observeState(): LiveData<PlaylistViewState> = stateLiveData

}