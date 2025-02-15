package com.practicum.playlistmaker.search.viewmodel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.MusicLocalIterator
import com.practicum.playlistmaker.search.domain.api.MusicNetworkIterator
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.viewmodel.state.TrackState
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    private val interactorNetwork: MusicNetworkIterator,
    private val iteratorLocal: MusicLocalIterator
) : ViewModel() {

    private val _state = MutableLiveData<TrackState>()
    val state: LiveData<TrackState> get() = _state

    fun searchMusic(changedText: String) {
        _state.value = TrackState.Loading

        viewModelScope.launch {
            interactorNetwork.searchTrack(changedText).collect{ pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    fun getListHistorySearchMusic() {
        _state.postValue(TrackState.History(iteratorLocal.get()))
    }

    fun setToListHistorySearchMusic(track: Track) {
        iteratorLocal.set(track)
        _state.postValue(TrackState.History(iteratorLocal.get()))
    }

    fun removeListHistorySearchMusic() {
        iteratorLocal.remove()
        _state.postValue(TrackState.History(iteratorLocal.get()))
    }

    private fun processResult(foundMusic: List<Track>?, errorMessage: String?){
        val tracks = mutableListOf<Track>()
        if (foundMusic != null){
            tracks.addAll(foundMusic)
        }
        when {
            errorMessage != null -> {
                setStateToView(TrackState.Error(errorMessage))
            }
            tracks.isEmpty() -> {
                setStateToView(TrackState.Empty)
            }
            else -> {
                setStateToView(TrackState.Content(tracks))
            }
        }

    }

    private fun setStateToView(state: TrackState) {
        _state.postValue(state)
    }

}