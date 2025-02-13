package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.MusicNetworkIterator
import com.practicum.playlistmaker.search.domain.api.MusicNetworkRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MusicNetworkIteratorImpl(
    private val repository: MusicNetworkRepository
) : MusicNetworkIterator {

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchMusic(expression).map { result ->
            when(result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.errorMessage)
            }
        }
    }
}
