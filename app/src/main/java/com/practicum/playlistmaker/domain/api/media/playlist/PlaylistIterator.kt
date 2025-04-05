package com.practicum.playlistmaker.domain.api.media.playlist

import android.net.Uri
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistIterator {

    //Добавление фотографии
    suspend fun saveImageToPrivateStorage(uri: Uri)

    // Добавление существующего плейлиста
    suspend fun insert(playlist: Playlist)

    //Обновить плейлист
    suspend fun update(playlist: Playlist)

    // Получение всех плейлистов
    fun getPlaylists(): Flow<List<Playlist>>

    // Получение плейлиста по Id
    suspend fun getPlaylistById(playlistId: Long): Playlist

    // Добавление трека в плейлист
    suspend fun setTrack(playlist: Playlist,track: Track)

    //Получение треков из плейлиста
    fun getAllTracks(trackIdsJson: String): Flow<List<Track>>

    //Удаление трека из плейлиста
    suspend fun removeTrackFromPlaylist(playlist: Playlist, trackId: Int): Playlist

    //Удаление плейлиста по id
    suspend fun deletePlaylist(playlist: Playlist)

}