package com.practicum.playlistmaker.domain.api.media

import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface FavouriteTrackRepository {

    suspend fun insertFavouriteTrack(track: Track)

    suspend fun deleteFavouriteTrack(track: Track)

    suspend fun getIndicatorsFavouriteTracks(): List<Int>

    fun getListFavouriteTracks(): Flow<List<Track>>

}