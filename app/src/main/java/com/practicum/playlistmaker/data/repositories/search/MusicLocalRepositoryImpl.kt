package com.practicum.playlistmaker.data.repositories.search

import com.practicum.playlistmaker.data.repositories.search.converter.TrackConverter
import com.practicum.playlistmaker.data.repositories.search.local.LocalStorage
import com.practicum.playlistmaker.domain.api.search.MusicLocalRepository
import com.practicum.playlistmaker.domain.model.Track

class MusicLocalRepositoryImpl(
    private val localStorage: LocalStorage,
    private val converterTrack: TrackConverter): MusicLocalRepository {

    override fun getHistoryMusic():List<Track> {
        return localStorage.getHistoryMusic().map { trackDto ->
            converterTrack.execute(trackDto)
        }
    }

    override fun setTrackToHistoryMusic(track: Track) {
       return localStorage.setMusicToHistory(converterTrack.execute(track))
    }

    override fun removeHistoryMusic() {
        localStorage.removeHistoryMusic()
    }
}