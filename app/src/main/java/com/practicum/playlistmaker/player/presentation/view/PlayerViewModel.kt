package com.practicum.playlistmaker.player.presentation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.player.domain.api.PlayerStatusListener
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val iterator: MediaPlayerIterator) : ViewModel() {
    private val _playerState = MutableLiveData<PlayerState>().apply { value = PlayerState() }
    val playerState: LiveData<PlayerState> get() = _playerState

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

    ///
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
}