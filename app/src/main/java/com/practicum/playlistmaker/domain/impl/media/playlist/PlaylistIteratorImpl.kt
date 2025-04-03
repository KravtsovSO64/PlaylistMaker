package com.practicum.playlistmaker.domain.impl.media.playlist

import android.net.Uri
import com.practicum.playlistmaker.domain.api.media.playlist.PlaylistIterator
import com.practicum.playlistmaker.domain.api.media.playlist.PlaylistRepository
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistIteratorImpl(
    private val repository: PlaylistRepository
): PlaylistIterator {

    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        repository.saveImageToPrivateStorage(uri)
    }

    override suspend fun insert(playlist: Playlist) {
        repository.insert(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun setTrack(playlist: Playlist,track: Track) {
        repository.setTrack(playlist, track)
    }
}