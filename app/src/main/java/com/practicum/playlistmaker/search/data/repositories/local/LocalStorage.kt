package com.practicum.playlistmaker.search.data.repositories.local

import com.practicum.playlistmaker.search.data.dto.TrackDto


interface LocalStorage {
    fun getHistoryMusic(): List<TrackDto>
    fun setMusicToHistory(track: TrackDto)
    fun removeHistoryMusic()
}