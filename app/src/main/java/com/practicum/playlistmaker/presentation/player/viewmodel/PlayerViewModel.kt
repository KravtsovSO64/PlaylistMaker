package com.practicum.playlistmaker.presentation.player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.api.media.favorite.FavouriteTrackIterator
import com.practicum.playlistmaker.domain.api.media.playlist.PlaylistIterator
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.state.AddTrackStatus
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState
import com.practicum.playlistmaker.presentation.player.service.AudioPlayerControl
import com.practicum.playlistmaker.presentation.player.state.PlayerState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val iteratorPlaylist: PlaylistIterator,
    private val iteratorFavouriteTrack: FavouriteTrackIterator
    ) : ViewModel() {

    private lateinit var track: Track

    private var currentPlaylists: List<Playlist> = emptyList()

    //Реализовывает связь с Service type is bound
    private var audioPlayerControl: AudioPlayerControl? = null

    //Состояние плеера
    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val playerState: LiveData<PlayerState> get() = _playerState

    //Состояние плейлиста
    private val _playlistsState = MutableLiveData<PlaylistViewState>()
    val playlistState: LiveData<PlaylistViewState> get() = _playlistsState

    private val _addTrackStatus = MutableLiveData<AddTrackStatus>()
    val addTrackStatus: LiveData<AddTrackStatus> get() = _addTrackStatus

    //Для хранения состояния находится трек в избранном или нет
    private val _isFavoriteTrack = MutableLiveData<Boolean>()
    val isFavoriteTrack: LiveData<Boolean> get() = _isFavoriteTrack

    fun setTrack(track: Track) {
        this.track = track

        viewModelScope.launch {
            val favoriteListId = iteratorFavouriteTrack.getIndicatorsFavouriteTracks()
            if (favoriteListId.contains(track.trackId)) {
                this@PlayerViewModel.track.isFavorite = true
                _isFavoriteTrack.postValue(track.isFavorite)
            }
        }
    }

    fun setAudioPlayerControl(controller: AudioPlayerControl) {
        audioPlayerControl = controller

        //Подписываемся на поток состояния Service в процессе модифицируем поток
        viewModelScope.launch {
            audioPlayerControl?.getPlayerState()
                ?.map { currentState ->
                    when (currentState) {
                        is PlayerState.Default -> currentState.copy(isFavourite = track.isFavorite)
                        is PlayerState.Prepared -> currentState.copy(isFavourite = track.isFavorite)
                        is PlayerState.Playing -> currentState.copy(isFavourite = track.isFavorite)
                        is PlayerState.Paused -> currentState.copy(isFavourite = track.isFavorite)
                    }
                }
                ?.collect { state ->
                    _playerState.postValue(state)
                }
        }
    }

    fun onPlayerButtonClicked() {
        if (playerState.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }

    fun showNotification() {
      audioPlayerControl?.showNotification()
    }

    fun hideNotification() {
        audioPlayerControl?.hideNotification()
    }

    //Метод для добавления трека в избранное
    fun addTrackToFavorite(track: Track) {

        this@PlayerViewModel.track.isFavorite = true

        viewModelScope.launch {
            iteratorFavouriteTrack.insertFavouriteTrack(track)
            track.isFavorite = true
            _isFavoriteTrack.postValue(this@PlayerViewModel.track.isFavorite)
        }
    }

    //Удаление трека из избранного
    fun deleteTrackFromFavourite(track: Track) {

        this@PlayerViewModel.track.isFavorite = false

        viewModelScope.launch {
            iteratorFavouriteTrack.deleteFavouriteTrack(track)
            track.isFavorite = false
            _isFavoriteTrack.postValue(this@PlayerViewModel.track.isFavorite)
        }
    }

    //Получение всех плейлистов
    fun getPlaylists() {
        viewModelScope.launch {
            iteratorPlaylist
                .getPlaylists()
                .collect {
                    currentPlaylists = it
                    processResult(currentPlaylists)
                }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    private fun processResult(playlist: List<Playlist>) {
        if (playlist.isEmpty()) {
            _playlistsState.postValue(PlaylistViewState.Empty(true))
        } else {
            _playlistsState.postValue(PlaylistViewState.Content(playlist, false))
        }
    }

    fun addTrackToPlaylist(playlistId: Long, track: Track) {
        //Нашли плейлист
        val playlist = currentPlaylists.find { it.id == playlistId }

        //Если плейлист не пустой, то получаем список id треков
        if (playlist != null) {
            val listIdTracks = getTrackIdsList(playlist.trackIdsJson)

            //Ищем id в списке, в зависимости от этого создаем состояние экрана
            if (track.trackId in listIdTracks) {
                _addTrackStatus.postValue(AddTrackStatus.AlreadyExists(playlist.name))
            } else {
                _addTrackStatus.postValue(AddTrackStatus.Success(playlist.name))
                viewModelScope.launch {
                    iteratorPlaylist.setTrack(playlist, track)
                }
            }
        }
    }

    private fun getTrackIdsList(trackIdsJson: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(trackIdsJson, type) ?: emptyList()
    }

}