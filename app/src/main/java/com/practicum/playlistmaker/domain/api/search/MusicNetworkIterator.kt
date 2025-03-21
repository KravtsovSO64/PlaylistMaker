package com.practicum.playlistmaker.domain.api.search

import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicNetworkIterator {

    fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>>

}