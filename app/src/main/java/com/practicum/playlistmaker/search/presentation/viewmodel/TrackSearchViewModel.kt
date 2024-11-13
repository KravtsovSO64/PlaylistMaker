package com.practicum.playlistmaker.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.data.dto.Result
import com.practicum.playlistmaker.search.domain.api.MusicNetworkInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.state.TrackSearchViewState

class TrackSearchViewModel : ViewModel() {
    private val _state = MutableLiveData<TrackSearchViewState>()
    val state: LiveData<TrackSearchViewState> get() = _state

    private val interactorNetwork = Creator.provideMusicInteractor()
    private val iteratorLocal = Creator.provideMusicLocalIterator()

    fun searchMusic(term: String) {
        _state.value = TrackSearchViewState.Loading

        interactorNetwork.searchTrack(term, object : MusicNetworkInteractor.MusicConsumer {
            override fun consumer(result: Result<List<Track>>) {
                when (result) {
                    is Result.Success -> {
                        _state.postValue(TrackSearchViewState.Content(result.data, result.code))
                    }
                    is Result.Failure -> {
                        _state.postValue(TrackSearchViewState.Error(result.code))
                    }
                }
            }
        })
    }

    fun getListHistorySearchMusic() {
        _state.postValue(TrackSearchViewState.History(iteratorLocal.get()))
    }

    fun setToListHistorySearchMusic(track: Track) {
        iteratorLocal.set(track)
        _state.postValue(TrackSearchViewState.History(iteratorLocal.get()))
    }

    fun removeListHistorySearchMusic() {
        iteratorLocal.remove()
        _state.postValue(TrackSearchViewState.History(iteratorLocal.get()))
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    TrackSearchViewModel()
                }
            }
        }
    }
}