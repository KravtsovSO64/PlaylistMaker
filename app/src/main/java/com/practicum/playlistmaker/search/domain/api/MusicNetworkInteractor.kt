package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.data.dto.Result
import com.practicum.playlistmaker.search.domain.models.Track

interface MusicNetworkInteractor {
    interface MusicConsumer {
        fun consumer(result: Result<List<Track>>)
    }

    fun searchTrack(expression: String, consumer: MusicConsumer)
}