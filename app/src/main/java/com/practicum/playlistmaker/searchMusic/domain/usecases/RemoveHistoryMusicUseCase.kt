package com.practicum.playlistmaker.searchMusic.domain.usecases

import com.practicum.playlistmaker.searchMusic.domain.repository.HistoryRepository

class RemoveHistoryMusicUseCase(private val repository: HistoryRepository) {
    fun execute() {
        repository.removeHistoryMusic()
    }
}