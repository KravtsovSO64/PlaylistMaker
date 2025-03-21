package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.data.db.entities.FavouriteTrackEntity

@Database(version = 1, entities = [FavouriteTrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun favouriteTrackDao(): FavouriteTrackDao
}