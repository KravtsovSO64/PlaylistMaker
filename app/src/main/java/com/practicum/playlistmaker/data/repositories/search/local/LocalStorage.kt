package com.practicum.playlistmaker.data.repositories.search.local

import com.practicum.playlistmaker.data.dto.search.TrackDto


interface LocalStorage {
    fun getHistoryMusic(): List<TrackDto>
    fun setMusicToHistory(track: TrackDto)
    fun removeHistoryMusic()
}