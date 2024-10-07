package com.practicum.playlistmaker.searchMusic.domain.repository

import com.practicum.playlistmaker.searchMusic.domain.api.PlayerStatusListener

interface MediaPlayerRepository {
    fun play(audioUrl: String)
    fun pause()
    fun stop()
    fun isPlaying(): Boolean
    fun currentPosition(): Int
    fun resume()
    fun setPlayerStatusListener(listener: PlayerStatusListener)
}