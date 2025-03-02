package com.practicum.playlistmaker.domain.api.media

import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTrackIterator {

    suspend fun insertFavouriteTrack(track: Track)

    suspend fun deleteFavouriteTrack(track: Track)

    fun getListFavouriteTracks(): Flow<List<Track>>
    
}