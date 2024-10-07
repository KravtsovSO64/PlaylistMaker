package com.practicum.playlistmaker.searchMusic.domain.api

interface MediaPlayerIterator {
    fun play(audioUrl: String)
    fun pause()
    fun stop()
    fun isPlaying(): Boolean
    fun currentPosition(): Int
    fun resume()
    fun setupPlayerStatusListener(statusListener: PlayerStatusListener)
}