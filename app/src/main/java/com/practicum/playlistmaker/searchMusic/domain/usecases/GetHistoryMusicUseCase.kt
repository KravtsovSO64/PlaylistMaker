package com.practicum.playlistmaker.searchMusic.domain.usecases


import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicLocalRepository


class GetHistoryMusicUseCase(private val repository: MusicLocalRepository) {
    fun execute(): List<Track> {
        return repository.getHistoryMusic()
    }
}