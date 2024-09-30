package com.practicum.playlistmaker.searchMusic.data.repositories.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto
import java.util.LinkedList

class SharedPrefsMusicStorage(context: Context): LocalStorage {

    private val sharedPreferences = context.getSharedPreferences(HISTORY_SEARCH, Context.MODE_PRIVATE)
    private var list = getListFromMemory(sharedPreferences)
    private val maxSizeList: Int = 10

    //Получить из локального хранилища список
    override fun getHistoryMusic(): List<TrackDto> {
        return getListFromMemory(sharedPreferences).toList().map { track ->
            TrackDto(
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                trackId = track.trackId,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
        }
    }

    //Добавить и сохранить новый список в локальном хранилище
    override fun setMusicToHistory(track: TrackDto) {
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.trackId == track.trackId) {
                iterator.remove()
                break
            }
        }
        if (list.size < maxSizeList){
            list.addFirst(track)
        } else {
            list.removeLast()
            list.addFirst(track)
        }
        setListToMemory(sharedPreferences)
    }

    //Очистить и сохранить новый список в локальном хранилище
    override fun removeHistoryMusic() {
        list.clear()
        setListToMemory(sharedPreferences)
    }

    // получение списка localStorage
    private fun getListFromMemory(sharedPreferences: SharedPreferences) : LinkedList<TrackDto> {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_HISTORY_SEARCH, null)
        return gson.fromJson(json, object : TypeToken<LinkedList<TrackDto>>() {}.type) ?: LinkedList<TrackDto>()
    }

    // сохранение списка localStorage
    private fun setListToMemory(sharedPreferences: SharedPreferences) {
        val json = Gson().toJson(list)
        sharedPreferences.edit()
            .putString(KEY_HISTORY_SEARCH, json)
            .apply()
    }

    companion object {
        private const val HISTORY_SEARCH = "history_search"
        private const val KEY_HISTORY_SEARCH = "key_history_search"
    }
}