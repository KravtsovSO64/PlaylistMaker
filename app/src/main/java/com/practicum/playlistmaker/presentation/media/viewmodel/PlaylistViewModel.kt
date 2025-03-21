package com.practicum.playlistmaker.presentation.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState

class PlaylistViewModel() : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistViewState>()
    fun observeState(): LiveData<PlaylistViewState> = stateLiveData

}