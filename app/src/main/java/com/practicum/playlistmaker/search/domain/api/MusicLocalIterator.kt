package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface MusicLocalIterator {

    fun get() : List<Track>
    fun set(track: Track)
    fun remove()
}