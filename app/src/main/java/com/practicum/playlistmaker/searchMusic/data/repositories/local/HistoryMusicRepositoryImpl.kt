package com.practicum.playlistmaker.searchMusic.data.repositories.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.repository.HistoryRepository
import java.util.LinkedList

class HistoryMusicRepositoryImpl(context: Context): HistoryRepository {

    private val sharedPreferences = context.getSharedPreferences(HISTORY_SEARCH, Context.MODE_PRIVATE)
    private var list = getListFromMemory(sharedPreferences)
    private val maxSizeList: Int = 10

     override fun getHistoryMusic(): List<Track> {
       return list.toList()
    } //получить список истории для RecyclerView

     override fun setMusicToHistory(track: Track) {
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
    } //добавить музыку в список истории и сохранить local

     override fun removeHistoryMusic() {
        list.clear()
        setListToMemory(sharedPreferences)
    } //очистить список истории

    private fun getListFromMemory(sharedPreferences: SharedPreferences) : LinkedList<Track> {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_HISTORY_SEARCH, null)
        return gson.fromJson(json, object : TypeToken<LinkedList<Track>>() {}.type) ?: LinkedList<Track>()
    } // получение списка local

    private fun setListToMemory(sharedPreferences: SharedPreferences) {
        val json = Gson().toJson(list)
        sharedPreferences.edit()
            .putString(KEY_HISTORY_SEARCH, json)
            .apply()
    } // сохранение списка local

    companion object {
        private const val HISTORY_SEARCH = "history_search"
        private const val KEY_HISTORY_SEARCH = "key_history_search"
    } //константы
}

