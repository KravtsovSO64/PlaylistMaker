package com.practicum.playlistmaker.searchMusic.domain.usecases

import com.practicum.playlistmaker.searchMusic.creator.MediaPlayerManager

class MediaPlayerUseCase(private val mediaPlayerManager: MediaPlayerManager) {
    fun prepareMedia(previewUrl: String) {
        mediaPlayerManager.prepareMedia(previewUrl)
    }

    fun start() {
        mediaPlayerManager.startPlayback()
    }

    fun pause() {
        mediaPlayerManager.pausePlayback()
    }

    fun release() {
        mediaPlayerManager.release()
    }
}