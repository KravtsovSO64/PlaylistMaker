package com.practicum.playlistmaker.searchMusic.presentation.uiComponents

import com.practicum.playlistmaker.searchMusic.domain.usecases.MediaPlayerUseCase

class PlayerPresenter(private val mediaPlayerUseCase: MediaPlayerUseCase) {
    fun prepareTrack(previewUrl: String) {
        mediaPlayerUseCase.prepareMedia(previewUrl)
    }

    fun play() {
        mediaPlayerUseCase.start()
    }

    fun pause() {
        mediaPlayerUseCase.pause()
    }

    fun release() {
        mediaPlayerUseCase.release()
    }
}