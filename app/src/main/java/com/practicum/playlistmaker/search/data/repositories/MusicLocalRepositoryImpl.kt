package com.practicum.playlistmaker.search.data.repositories

import com.practicum.playlistmaker.search.data.repositories.local.LocalStorage
import com.practicum.playlistmaker.search.data.repositories.mapper.MapperTrackDtoFromTrack
import com.practicum.playlistmaker.search.data.repositories.mapper.MapperTrackFromTrackDto
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.MusicLocalRepository

class MusicLocalRepositoryImpl(private val localStorage: LocalStorage): MusicLocalRepository {

    override fun getHistoryMusic(): List<Track> {
        return localStorage.getHistoryMusic().map {
            MapperTrackFromTrackDto().execute(it)
        }
    }

    override fun setTrackToHistoryMusic(track: Track) {
       return localStorage.setMusicToHistory(MapperTrackDtoFromTrack().execute(track))
    }

    override fun removeHistoryMusic() {
        localStorage.removeHistoryMusic()
    }


}