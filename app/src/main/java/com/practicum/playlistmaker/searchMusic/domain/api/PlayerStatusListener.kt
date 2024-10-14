package com.practicum.playlistmaker.searchMusic.domain.api

interface PlayerStatusListener {
    fun onPlayerStatusChanged(status: PlayerStatus)
}

enum class PlayerStatus {
    PLAYING,
    PAUSED,
    STOPPED,
    COMPLETED,
    PREPARING,
    ERROR
}