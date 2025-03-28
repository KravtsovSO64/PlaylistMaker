package com.practicum.playlistmaker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImagePath: String = "",
    val trackIdsJson: String = "[]",
    val trackCount: Int = 0
) {
    // Функция для получения списка ID треков
    fun getTrackIds(): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson(trackIdsJson, type) ?: emptyList()
    }

    // Функция для установки списка ID треков
    fun setTrackIds(ids: List<Long>): PlaylistEntity {
        val json = Gson().toJson(ids)
        return this.copy(trackIdsJson = json, trackCount = ids.size)
    }

}