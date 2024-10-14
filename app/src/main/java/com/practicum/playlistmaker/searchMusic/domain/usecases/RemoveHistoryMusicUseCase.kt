package com.practicum.playlistmaker.searchMusic.domain.usecases

import com.practicum.playlistmaker.searchMusic.domain.repository.MusicLocalRepository

class RemoveHistoryMusicUseCase(private val repository: MusicLocalRepository) {
    fun execute() {
        repository.removeHistoryMusic()
    }
}