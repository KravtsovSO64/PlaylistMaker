package com.practicum.playlistmaker.searchMusic.data.repositories.local

import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto

class SharedPreferencesHistoryMusicClient(): HistoryMusicLocalService {

    private val maxSizeList = 10
    private val linkedList = getListFromMemory(sharedPreferences) // Ваш метод чтения из SharedPreferences

    override fun getHistory(): List<Track> {
        return linkedList.toList()
    }

    override fun addTrack(track: TrackDto) {
        linkedList.removeIf { it.trackId == track.trackId }

        if (linkedList.size >= maxSizeList) {
            linkedList.removeLast()
        }
        linkedList.addFirst(track)
        saveListToMemory(sharedPreferences, linkedList) // Ваш метод записи в SharedPreferences
    }
}