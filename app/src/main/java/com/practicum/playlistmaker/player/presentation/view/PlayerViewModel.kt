package com.practicum.playlistmaker.player.presentation.view

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.player.domain.api.PlayerStatusListener
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val iterator: MediaPlayerIterator) : ViewModel() {
    private val _playerState = MutableLiveData<PlayerState>().apply { value = PlayerState() }
    val playerState: LiveData<PlayerState> get() = _playerState

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
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (iterator.isPlaying()) {
                    val currentTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(iterator.currentPosition())
                    val currentState = _playerState.value ?: PlayerState()
                    _playerState.value = currentState.copy(currentPosition = currentTime)
                }
                handler.postDelayed(this, 300)
            }
        })
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