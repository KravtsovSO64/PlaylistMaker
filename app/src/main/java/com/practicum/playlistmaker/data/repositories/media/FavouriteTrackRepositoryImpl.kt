package com.practicum.playlistmaker.data.repositories.media

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.converter.TrackDbConverter
import com.practicum.playlistmaker.data.db.entities.FavouriteTrackEntity
import com.practicum.playlistmaker.domain.api.media.FavouriteTrackRepository
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConverter
): FavouriteTrackRepository {

    override suspend fun insertFavouriteTrack(track: Track) {
        appDatabase.favouriteTrackDao().insertTrack(converter.map(track))
    }

    override suspend fun deleteFavouriteTrack(track: Track) {
        appDatabase.favouriteTrackDao().deleteTrack(converter.map(track))
    }

    override fun getListFavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favouriteTrackDao().getListFavouriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<FavouriteTrackEntity>): List<Track> {
        return tracks.map { track -> converter.map(track) }
    }

}
