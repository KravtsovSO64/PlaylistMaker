package com.practicum.playlistmaker.presentation.player.service

import com.practicum.playlistmaker.presentation.player.state.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun showNotification()
    fun hideNotification()
}