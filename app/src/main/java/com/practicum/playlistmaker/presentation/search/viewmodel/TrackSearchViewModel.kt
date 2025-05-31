package com.practicum.playlistmaker.presentation.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.search.MusicLocalIterator
import com.practicum.playlistmaker.domain.api.search.MusicNetworkIterator
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.search.state.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    private val interactorNetwork: MusicNetworkIterator,
    private val iteratorLocal: MusicLocalIterator,
) : ViewModel() {

    private var searchJob: Job? = null

    //LiveData for State View
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    //Observer text in TextField
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText.asStateFlow()

    fun onTextChanged(newText: String) {
        _searchText.value = newText

        searchJob?.cancel()

        if (newText.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchMusic(newText)
            }
        } else {
            getListHistorySearchMusic()
        }
    }

     fun searchMusic(changedText: String) {
        _state.value = State.Loading

        viewModelScope.launch {
            interactorNetwork.searchTrack(changedText).collect{ pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    fun getListHistorySearchMusic() {
        viewModelScope.launch {
            _state.postValue(State.History(iteratorLocal.get()))
        }
    }

    fun setToListHistorySearchMusic(track: Track) {
        iteratorLocal.set(track)
        viewModelScope.launch {
            _state.postValue(State.History(iteratorLocal.get()))
        }
    }

    fun removeListHistorySearchMusic() {
        iteratorLocal.remove()
        viewModelScope.launch {
            _state.postValue(State.History(iteratorLocal.get()))
        }

    }

    private fun processResult(foundMusic: List<Track>?, errorMessage: String?){
        val tracks = mutableListOf<Track>()
        if (foundMusic != null){
            tracks.addAll(foundMusic)
        }
        when {
            errorMessage != null -> {
                setStateToView(State.Error(errorMessage))
            }
            tracks.isEmpty() -> {
                setStateToView(State.Empty)
            }
            else -> {
                setStateToView(State.Content(tracks))
            }
        }

    }

    private fun setStateToView(state: State) {
        _state.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}