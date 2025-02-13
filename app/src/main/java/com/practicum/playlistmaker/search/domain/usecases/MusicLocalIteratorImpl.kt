package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.MusicLocalIterator
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.MusicLocalRepository

class MusicLocalIteratorImpl(private val repository: MusicLocalRepository) : MusicLocalIterator {
    override fun get() : List<Track> {
        return repository.getHistoryMusic()
    }

    override fun set(track: Track) {
        repository.setTrackToHistoryMusic(track)
    }

    override fun remove() {
        repository.removeHistoryMusic()
    }
}