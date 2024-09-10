package com.practicum.playlistmaker.searchMusic.domain.usecases

import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.HistoryRepository

class SetHistoryMusicUseCase(private val repository: HistoryRepository) {
    fun execute(track: Track){
           repository.setMusicToHistory(track)
    }
}