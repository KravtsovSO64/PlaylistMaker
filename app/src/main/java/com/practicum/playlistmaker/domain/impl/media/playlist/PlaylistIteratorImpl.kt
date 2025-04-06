package com.practicum.playlistmaker.domain.impl.media.playlist

import android.net.Uri
import com.practicum.playlistmaker.data.db.entities.PlaylistEntity
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

    override suspend fun update(playlist: Playlist) {
        repository.update(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun setTrack(playlist: Playlist,track: Track) {
        repository.setTrack(playlist, track)
    }

    override fun getAllTracks(trackIdsJson: String): Flow<List<Track>> {
        return repository.getAllTracks(trackIdsJson)
    }

    override suspend fun removeTrackFromPlaylist(playlist: Playlist, trackId: Int): Playlist {
       return repository.removeTrackFromPlaylist(playlist, trackId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }


}