package com.practicum.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.api.PlayerStatusListener
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel: ViewModel() {

    private val _isPlaying = MutableLiveData<Boolean>().apply { value = false }
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _currentPosition = MutableLiveData<String>().apply { value = "00:00" }
    val currentPosition: LiveData<String> get() = _currentPosition

    private val _audioUrl = MutableLiveData<String>()
    val audioUrl: LiveData<String> get() = _audioUrl

    private val iterator = Creator.provideMediaPlayerInteractor()

    fun setAudioUrl(url: String) {
        _audioUrl.value = url
    }

    private fun play() {
        _audioUrl.value?.let {
            iterator.play(it)
            _isPlaying.value = true
            startUpdatingCurrentPosition()
        }
    }

    private fun pause() {
        iterator.pause()
        _isPlaying.value = false
    }

    fun stop() {
        _isPlaying.value = false
        _currentPosition.value = "00:00"
        iterator.stop()
    }

    fun togglePlayback() {
        if (_isPlaying.value == true) pause() else play()
    }

    private fun startUpdatingCurrentPosition() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (iterator.isPlaying()) {
                    _currentPosition.value = SimpleDateFormat("mm:ss", Locale.getDefault()).format(iterator.currentPosition())
                }
                handler.postDelayed(this, 300)
            }
        })
    }

    fun setupListeners() {
        iterator.setupPlayerStatusListener(object : PlayerStatusListener {
            override fun onPlaybackCompleted() {
                _isPlaying.value = false
                _currentPosition.value = "00:00"
            }
        })
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerViewModel()
                }
            }
        }
    }
}