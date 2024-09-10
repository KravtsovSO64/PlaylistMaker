package com.practicum.playlistmaker.searchMusic.domain.repository

import com.practicum.playlistmaker.searchMusic.domain.models.Track

interface HistoryRepository {
    fun getHistoryMusic(): List<Track>
    fun setMusicToHistory(track: Track)
    fun removeHistoryMusic()
}