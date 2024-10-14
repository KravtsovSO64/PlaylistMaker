package com.practicum.playlistmaker.searchMusic.domain.usecases

import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicLocalRepository

class SetHistoryMusicUseCase(private val repository: MusicLocalRepository) {
    fun execute(track: Track){
           repository.setTrackToHistoryMusic(track)
    }
}