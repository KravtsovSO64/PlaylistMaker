package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.data.db.entities.FavouriteTrackEntity
import com.practicum.playlistmaker.data.db.entities.PlaylistEntity

@Database(
    version = 1,
    entities = [
        FavouriteTrackEntity::class,
        PlaylistEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun favouriteTrackDao(): FavouriteTrackDao

    abstract fun playlistDao(): PlaylistDao

}