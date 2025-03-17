package com.practicum.playlistmaker.presentation.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.media.FavouriteTrackIterator
import com.practicum.playlistmaker.domain.api.search.MusicLocalIterator
import com.practicum.playlistmaker.domain.api.search.MusicNetworkIterator
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.search.state.TrackState
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    private val interactorNetwork: MusicNetworkIterator,
    private val iteratorLocal: MusicLocalIterator,
    private val iteratorFavourite: FavouriteTrackIterator
) : ViewModel() {

    //LiveData for State View
    private val _state = MutableLiveData<TrackState>()
    val state: LiveData<TrackState> get() = _state

    //LiveData for index tracks is favourite
    private val _isFavorite = MutableLiveData<List<Int>>()
    val isFavorite: LiveData<List<Int>> get() = _isFavorite


    fun searchMusic(changedText: String) {
        _state.value = TrackState.Loading

        viewModelScope.launch {
            interactorNetwork.searchTrack(changedText).collect{ pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    fun getListHistorySearchMusic() {
        viewModelScope.launch {
            _state.postValue(TrackState.History(iteratorLocal.get()))
        }
    }

    fun setToListHistorySearchMusic(track: Track) {
        iteratorLocal.set(track)
        viewModelScope.launch {
            _state.postValue(TrackState.History(iteratorLocal.get()))
        }
    }

    fun removeListHistorySearchMusic() {
        iteratorLocal.remove()
        viewModelScope.launch {
            _state.postValue(TrackState.History(iteratorLocal.get()))
        }

    }

    fun currentIndexesFavouriteTracks() {
        viewModelScope.launch {
            _isFavorite.postValue(iteratorFavourite.getIndicatorsFavouriteTracks())
        }
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