package com.practicum.playlistmaker.data.repositories.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.converter.PlaylistDbConverter
import com.practicum.playlistmaker.data.db.entities.PlaylistEntity
import com.practicum.playlistmaker.domain.api.media.playlist.PlaylistRepository
import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase,
    private val converter: PlaylistDbConverter
): PlaylistRepository {

    override suspend fun insert(playlist: Playlist) {
        appDatabase.playlistDao().insert(converter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
      val playlist = appDatabase.playlistDao().getPlaylists()
        return convertFromPlaylistEntity(playlist)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myPlaylist")

        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(filePath, "first_cover.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun convertFromPlaylistEntity(playlistEntity: Flow<List<PlaylistEntity>>): Flow<List<Playlist>> {
        return playlistEntity.map { list ->
            list.map { playlist ->
                converter.map(playlist)
            }
        }
    }

}