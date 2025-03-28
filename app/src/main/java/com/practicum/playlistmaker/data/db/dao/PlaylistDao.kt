package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.data.db.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    // Добавление существующего плейлиста
    @Insert
    suspend fun insert(playlist: PlaylistEntity)

    // Обновление существующего плейлиста
    @Update
    suspend fun update(playlist: PlaylistEntity)

    // Получение всех плейлистов (Flow для наблюдения за изменениями)
    @Query("SELECT * FROM playlist_table ORDER BY id ASC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    // Специальные методы для работы со списком треков

    // Добавление трека в плейлист
    @Query("UPDATE playlist_table SET trackIdsJson = :newTrackIdsJson, trackCount = :newCount WHERE id = :playlistId")
    suspend fun addTrackToPlaylist(playlistId: Long, newTrackIdsJson: String, newCount: Int)

    // Удаление трека из плейлиста
    @Query("UPDATE playlist_table SET trackIdsJson = :newTrackIdsJson, trackCount = :newCount WHERE id = :playlistId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, newTrackIdsJson: String, newCount: Int)

    // Получение количества треков в плейлисте
    @Query("SELECT trackCount FROM playlist_table WHERE id = :id")
    suspend fun getTrackCount(id: Long): Int

}