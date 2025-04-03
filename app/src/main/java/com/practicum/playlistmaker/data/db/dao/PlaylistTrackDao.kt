package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entities.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {

    //Добавить трек
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setTrack(track: PlaylistTrackEntity)

    // Получение всех треков плейлиста
    @Query("SELECT * FROM playlist_track_table")
    fun getAllTracks(): Flow<List<PlaylistTrackEntity>>

    //Удаление трека
    @Query("DELETE FROM playlist_track_table WHERE trackId = :trackId")
    suspend fun removeTrack(trackId: Int)

}