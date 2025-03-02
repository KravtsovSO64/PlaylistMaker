package com.practicum.playlistmaker.domain.api.search

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MusicNetworkRepository {

    fun searchMusic(expression: String): Flow<Resource<List<Track>>>

}