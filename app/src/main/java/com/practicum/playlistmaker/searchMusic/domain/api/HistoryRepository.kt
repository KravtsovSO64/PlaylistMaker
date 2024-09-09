package com.practicum.playlistmaker.searchMusic.domain.api

import com.practicum.playlistmaker.searchMusic.domain.entities.Track
import java.util.LinkedList

interface HistoryRepository {
    fun getListHistory(): List<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getListFromMemory(): LinkedList<Track>
    fun setListToMemory()
}