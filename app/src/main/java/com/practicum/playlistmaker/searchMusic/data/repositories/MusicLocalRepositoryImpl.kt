package com.practicum.playlistmaker.searchMusic.data.repositories

import com.practicum.playlistmaker.searchMusic.data.repositories.local.LocalStorage
import com.practicum.playlistmaker.searchMusic.data.repositories.mapper.MapperTrackDtoFromTrack
import com.practicum.playlistmaker.searchMusic.data.repositories.mapper.MapperTrackFromTrackDto
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicLocalRepository

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