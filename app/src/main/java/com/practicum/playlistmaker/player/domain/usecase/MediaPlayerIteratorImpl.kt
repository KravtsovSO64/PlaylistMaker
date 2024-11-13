package com.practicum.playlistmaker.player.domain.usecase

import com.practicum.playlistmaker.player.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.player.domain.api.PlayerStatusListener
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository

class MediaPlayerIteratorImpl(private val mediaPlayerRepository: MediaPlayerRepository):
    MediaPlayerIterator {

    override fun play(audioUrl: String) {
        return mediaPlayerRepository.play(audioUrl)
    }

    override fun pause() {
        return mediaPlayerRepository.pause()
    }

    override fun stop() {
        return mediaPlayerRepository.stop()
    }

    override fun isPlaying(): Boolean {
      return mediaPlayerRepository.isPlaying()
    }

    override fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }

    override fun resume() {
        return mediaPlayerRepository.resume()
    }

    override fun setupPlayerStatusListener(statusListener: PlayerStatusListener) {
        mediaPlayerRepository.setPlayerStatusListener(statusListener)
    }

}