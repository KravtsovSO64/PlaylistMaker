package com.practicum.playlistmaker.domain.impl.media.favorite

import com.practicum.playlistmaker.domain.api.media.favorite.FavouriteTrackIterator
import com.practicum.playlistmaker.domain.api.media.favorite.FavouriteTrackRepository
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTrackIteratorImpl(
    private val repository: FavouriteTrackRepository
): FavouriteTrackIterator {

    override suspend fun insertFavouriteTrack(track: Track) {
        repository.insertFavouriteTrack(track)
    }

    override suspend fun deleteFavouriteTrack(track: Track) {
        repository.deleteFavouriteTrack(track)
    }

    override suspend fun getIndicatorsFavouriteTracks(): List<Int> {
        return repository.getIndicatorsFavouriteTracks()
    }

    override fun getListFavouriteTracks(): Flow<List<Track>> {
       return repository.getListFavouriteTracks()
    }

}