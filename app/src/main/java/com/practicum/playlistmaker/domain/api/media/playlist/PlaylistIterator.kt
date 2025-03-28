package com.practicum.playlistmaker.domain.api.media.playlist

import android.net.Uri
import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistIterator {

    //Добавление фотографии
    suspend fun saveImageToPrivateStorage(uri: Uri)

    // Добавление существующего плейлиста
    suspend fun insert(playlist: Playlist)

    // Получение всех плейлистов
    fun getPlaylists(): Flow<List<Playlist>>

}