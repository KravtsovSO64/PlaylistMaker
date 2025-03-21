package com.practicum.playlistmaker.domain.impl.search

import android.util.Log
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.api.search.MusicLocalIterator
import com.practicum.playlistmaker.domain.api.search.MusicLocalRepository
import com.practicum.playlistmaker.domain.model.Track

class MusicLocalIteratorImpl(
    private val repository: MusicLocalRepository,
    private val appDatabase: AppDatabase
) : MusicLocalIterator {

    override suspend fun get() : List<Track>  {
        val listHistory = repository.getHistoryMusic()
        val indexFavouriteTrack = appDatabase.favouriteTrackDao().getIndicatorsFavouriteTracks()
        Log.d("Test", indexFavouriteTrack.toString())
        return getListWithFavouriteTracks(indexFavouriteTrack, listHistory)
    }

    override fun set(track: Track) {
        repository.setTrackToHistoryMusic(track)
    }

    override fun remove() {
        repository.removeHistoryMusic()
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