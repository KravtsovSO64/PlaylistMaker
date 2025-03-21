package com.practicum.playlistmaker.domain.impl.search

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.api.search.MusicNetworkIterator
import com.practicum.playlistmaker.domain.api.search.MusicNetworkRepository
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MusicNetworkIteratorImpl(
    private val repository: MusicNetworkRepository,
    private val appDatabase: AppDatabase
) : MusicNetworkIterator {

        override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
            return repository.searchMusic(expression).map { result ->
                when(result) {
                    is Resource.Success -> {
                        val favouriteTrack = appDatabase.favouriteTrackDao().getIndicatorsFavouriteTracks()
                        Pair(getListWithFavouriteTracks(favouriteTrack, result.data), null)
                    }
                    is Resource.Error -> Pair(null, result.errorMessage)
                }
            }
        }

    private fun getListWithFavouriteTracks(trackFavoriteIds: List<Int>, trackList: List<Track>?): List<Track> {
        return trackList?.map { track ->
            if (track.trackId in trackFavoriteIds) {
                track.copy(isFavorite = true)
            } else {
                track
            }
        } ?: emptyList()
    }
}
