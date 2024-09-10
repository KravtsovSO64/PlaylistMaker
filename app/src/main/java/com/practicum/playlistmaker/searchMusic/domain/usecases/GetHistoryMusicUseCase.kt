package com.practicum.playlistmaker.searchMusic.domain.usecases


import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.HistoryRepository


class GetHistoryMusicUseCase(private val repository: HistoryRepository) {
    fun execute(): List<Track> {
        return repository.getHistoryMusic()
    }
}