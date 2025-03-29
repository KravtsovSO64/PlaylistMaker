package com.practicum.playlistmaker.presentation.player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.api.media.favorite.FavouriteTrackIterator
import com.practicum.playlistmaker.domain.api.media.playlist.PlaylistIterator
import com.practicum.playlistmaker.domain.api.player.MediaPlayerIterator
import com.practicum.playlistmaker.domain.api.player.PlayerStatusListener
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.state.AddTrackStatus
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState
import com.practicum.playlistmaker.presentation.player.state.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val iterator: MediaPlayerIterator,
    private val iteratorPlaylist: PlaylistIterator,
    private val iteratorFavouriteTrack: FavouriteTrackIterator
    ) : ViewModel() {

    private lateinit var track: Track
    private var currentPlaylists: List<Playlist> = emptyList()

    private val _playerState = MutableLiveData<PlayerState>().apply { value = PlayerState() }
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _playlistsState = MutableLiveData<PlaylistViewState>()
    val playlistState: LiveData<PlaylistViewState> get() = _playlistsState

    private val _addTrackStatus = MutableLiveData<AddTrackStatus>()
    val addTrackStatus: LiveData<AddTrackStatus> get() = _addTrackStatus

    private var timingJob: Job? = null

    fun setAudioUrl(url: String) {
        val currentState = _playerState.value ?: PlayerState()
        _playerState.value = currentState.copy(audioUrl = url)
    }

    private fun play() {
        _playerState.value?.audioUrl?.let {
            iterator.play(it)
            val currentState = _playerState.value ?: PlayerState()
            _playerState.value = currentState.copy(isPlaying = true)
            startUpdatingCurrentPosition()
        }
    }

    private fun pause() {
        iterator.pause()
        val currentState = _playerState.value ?: PlayerState()
        _playerState.value = currentState.copy(isPlaying = false)
    }

    fun stop() {
        iterator.stop()
        _playerState.value = PlayerState()
    }

    fun togglePlayback() {
        if (_playerState.value?.isPlaying == true) {
            pause()
        } else {
            play()
        }
    }

    private fun startUpdatingCurrentPosition() {
        timingJob = viewModelScope.launch {
            while (iterator.isPlaying()){
                delay(300L)
                if (iterator.isPlaying()) {
                    val currentTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(iterator.currentPosition())
                    val currentState = _playerState.value ?: PlayerState()
                    _playerState.value = currentState.copy(currentPosition = currentTime)
                }
            }
            timingJob?.cancel()
        }
    }

    fun setupListeners() {
        iterator.setupPlayerStatusListener(object : PlayerStatusListener {
            override fun onPlaybackCompleted() {
                val currentState = _playerState.value ?: PlayerState()
                _playerState.value = currentState.copy(isPlaying = false, currentPosition = "00:00")
            }
        })
    }

    fun setData(track: Track) {
        this.track = track
        val currentState = _playerState.value ?: PlayerState()
        _playerState.value = currentState.copy(isFavourite = track.isFavorite)
    }

    fun getFavouriteTrack(track: Track) {
        viewModelScope.launch {
            iteratorFavouriteTrack.insertFavouriteTrack(track)

            val currentState = _playerState.value ?: PlayerState()
            track.isFavorite = true
            _playerState.value = currentState.copy(isFavourite = true)
        }
    }

    fun deleteTrackFromFavourite(track: Track) {
        viewModelScope.launch {
            iteratorFavouriteTrack.deleteFavouriteTrack(track)

            val currentState = _playerState.value ?: PlayerState()
            track.isFavorite = false
            _playerState.value = currentState.copy(isFavourite = false)
        }
    }

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

    private fun processResult(playlist: List<Playlist>) {
        if (playlist.isEmpty()) {
            _playlistsState.postValue(PlaylistViewState.Empty(true))
        } else{
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