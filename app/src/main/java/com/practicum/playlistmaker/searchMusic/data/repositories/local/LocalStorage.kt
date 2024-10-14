package com.practicum.playlistmaker.searchMusic.data.repositories.local

import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto


interface LocalStorage {
    fun getHistoryMusic(): List<TrackDto>
    fun setMusicToHistory(track: TrackDto)
    fun removeHistoryMusic()
}