package com.practicum.playlistmaker.domain.api.search

import com.practicum.playlistmaker.domain.model.Track

interface MusicLocalIterator {

   suspend fun get() : List<Track>
    fun set(track: Track)
    fun remove()
}