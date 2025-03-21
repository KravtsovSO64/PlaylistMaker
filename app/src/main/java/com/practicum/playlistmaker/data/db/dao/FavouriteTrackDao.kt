package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entities.FavouriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteTrackDao {

    @Insert(entity = FavouriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavouriteTrackEntity)

    @Delete(entity = FavouriteTrackEntity::class)
    suspend fun deleteTrack(track: FavouriteTrackEntity)

    @Query("SELECT trackId FROM favourite_track_table" )
    suspend fun getIndicatorsFavouriteTracks(): List<Int>

    @Query("SELECT * FROM  favourite_track_table ORDER BY timestamp DESC")
    fun getListFavouriteTracks(): Flow<List<FavouriteTrackEntity>>

}