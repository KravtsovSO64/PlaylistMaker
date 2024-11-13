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
import com.practicum.playlistmaker.search.presentation.state.TrackSearchState

class TrackSearchViewModel: ViewModel() {
    private val _state = MutableLiveData<TrackSearchState>()
    val state: LiveData<TrackSearchState> get() = _state

    private val _listHistory = MutableLiveData<List<Track>>()
    val listHistory: LiveData<List<Track>> get() = _listHistory

    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track> get() = _track


    private val interactorNetwork = Creator.provideMusicInteractor()
    private val iteratorLocal = Creator.provideMusicLocalIterator()


    fun searchMusic(term: String) {
        _state.value = TrackSearchState.Loading // Показать состояние загрузки

        interactorNetwork.searchTrack(term, object : MusicNetworkInteractor.MusicConsumer {
            override fun consumer(result: Result<List<Track>>) {
                when (result) {
                    is Result.Success -> {
                        _state.postValue(TrackSearchState.Content(result.data, result.code))
                    }
                    is Result.Failure -> {
                        _state.postValue(TrackSearchState.Error(result.code))
                    }
                }
            }
        })
    }

    fun getListHistorySearchMusic() {
        _listHistory.postValue(iteratorLocal.get())
    }

    fun setToListHistorySearchMusic(track : Track) {
        iteratorLocal.set(track)
        _listHistory.postValue(iteratorLocal.get())
    }

    fun removeListHistorySearchMusic() {
        iteratorLocal.remove()
        _listHistory.postValue(iteratorLocal.get())
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