package com.practicum.playlistmaker.data.repositories.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.converter.PlaylistDbConverter
import com.practicum.playlistmaker.data.db.entities.PlaylistEntity
import com.practicum.playlistmaker.domain.api.media.playlist.PlaylistRepository
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
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

    override fun getAllTracks(trackIdsJson: String): Flow<List<Track>> {
        val trackIds  = convertJsonToList(trackIdsJson)

        return appDatabase.tracksFromPlaylist().getAllTracks()
            .map { playlistTracks ->
                playlistTracks
                    .filter { track -> trackIds.contains(track.trackId) }
                    .map { track -> converter.map(track) }
            }
    }

    override suspend fun removeTrackFromPlaylist(playlist: Playlist, trackId: Int): Playlist {
        val currentTrackIds = convertJsonToList(playlist.trackIdsJson).toMutableList()

        currentTrackIds.remove(trackId)

        appDatabase.playlistDao().removeTrackFromPlaylist(
            playlistId = playlist.id,
            newTrackIdsJson = convertListToJson(currentTrackIds),
            newCount = playlist.trackCount - 1)

        // Проверяем и удаляем трек, если он больше не используется
        checkAndRemoveUnusedTrack(trackId)

        return converter.map(appDatabase.playlistDao().getPlaylistById(playlist.id)!!)
    }

    override suspend fun setTrack(playlist: Playlist,track: Track) {

        //Добавление трека в отдельную таблицу playlist_track_table
        appDatabase.tracksFromPlaylist().setTrack(converter.map(track))

        //Теперь нам нужно обновить список треков и обновить счетчик
        appDatabase.playlistDao().addTrackToPlaylist(
            playlistId = playlist.id,
            newTrackIdsJson = updateTrackIdsJson(playlist.trackIdsJson, track),
            newCount = playlist.trackCount + 1)
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

    private fun updateTrackIdsJson(trackIdsJson: String, track: Track): String {

        //Получили список для дальнейшей работы
        val list = convertJsonToList(trackIdsJson).toMutableList()

        //Добавили id трека в список
        list.add(track.trackId)

        //Вернули строку
        return convertListToJson(list)
    }

    private fun convertJsonToList(trackIdsJson: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(trackIdsJson, type) ?: emptyList()
    }

    private fun convertListToJson(list: List<Int>): String {
        return Gson().toJson(list)
    }

    private suspend fun checkAndRemoveUnusedTrack(trackId: Int) {
        // Получаем все плейлисты
        val playlists = appDatabase.playlistDao().getAllPlaylistsSync()

        // Проверяем, есть ли этот трек в каком-либо плейлисте
        val isTrackUsed = playlists.any { playlist ->
            val trackIds = convertJsonToList(playlist.trackIdsJson)
            trackIds.contains(trackId)
        }

        // Если трек не используется ни в одном плейлисте, удаляем его
        if (!isTrackUsed) {
            appDatabase.tracksFromPlaylist().removeTrack(trackId)
        }
    }


}