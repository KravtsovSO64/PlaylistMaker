package com.practicum.playlistmaker.presentation.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.model.Track


class TrackViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var trackLiveData: MutableLiveData<Track> = savedStateHandle.getLiveData("TRACK_KEY")

    init {
        val track = savedStateHandle.get<Track>("TRACK_KEY")
        if (track != null) {

            trackLiveData.value = track
        }
    }

}