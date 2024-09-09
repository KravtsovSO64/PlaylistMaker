package com.practicum.playlistmaker.searchMusic.data.repositories.local

import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto

interface HistoryMusicLocalService {

    fun getHistory(): List<TrackDto>
    fun addTrack(track: TrackDto)

}