package com.practicum.playlistmaker.searchMusic.domain.api

import com.practicum.playlistmaker.searchMusic.domain.entities.Track

interface MusicInteractor {
    fun searchTrack(expression: String, consumer: MusicConsumer)

    interface MusicConsumer{
        fun consumer(foundMusic: List<Track>)
    }
}