package com.practicum.playlistmaker.domain.api.player

interface MediaPlayerRepository {
    fun play(audioUrl: String)
    fun pause()
    fun stop()
    fun isPlaying(): Boolean
    fun currentPosition(): Int
    fun resume()
    fun setPlayerStatusListener(statusListener: PlayerStatusListener)
}