package com.practicum.playlistmaker.domain.api.media.playlist

import android.net.Uri
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    //Добавление фотографии
    suspend fun saveImageToPrivateStorage(uri: Uri)

    // Добавление существующего плейлиста
    suspend fun insert(playlist: Playlist)

    // Получение всех плейлистов
    fun getPlaylists(): Flow<List<Playlist>>

    // Добавление трека в плейлист
    suspend fun setTrack(playlist: Playlist,track: Track)

    //Получение треков из плейлиста
    fun getAllTracks(trackIdsJson: String): Flow<List<Track>>

    //Удаление трека из плейлиста
    suspend fun removeTrackFromPlaylist(playlist: Playlist, trackId: Int): Playlist

}