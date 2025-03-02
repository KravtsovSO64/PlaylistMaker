package com.practicum.playlistmaker.domain.impl.media

import com.practicum.playlistmaker.domain.api.media.FavouriteTrackIterator
import com.practicum.playlistmaker.domain.api.media.FavouriteTrackRepository
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

    override fun getListFavouriteTracks(): Flow<List<Track>> {
       return repository.getListFavouriteTracks()
    }

}