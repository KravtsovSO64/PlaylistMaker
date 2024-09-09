package com.practicum.playlistmaker.searchMusic.domain.use_cases

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.searchMusic.data.LocalClient
import com.practicum.playlistmaker.searchMusic.domain.api.HistoryRepository
import com.practicum.playlistmaker.searchMusic.domain.entities.Track
import java.util.LinkedList

class HistoryRepositoryImpl(private val localClient: LocalClient) : HistoryRepository {

    private val maxSizeList: Int = 10
    var linkedList = getListFromMemory()

    override fun getListHistory(): List<Track> {
        return ArrayList(linkedList)
    }

    override fun addTrackToHistory(track: Track) {
        linkedList.removeIf { it.trackId == track.trackId }

        if (linkedList.size < maxSizeList) {
            linkedList.addFirst(track)
        } else {
            linkedList.removeLast()
            linkedList.addFirst(track)
        }

        setListToMemory()
    }

    override fun clearHistory() {
        linkedList.clear()
        setListToMemory()
    }

    override fun getListFromMemory(): LinkedList<Track> {
        val gson = Gson()
        if (localClient is SharedPreferences) {
            val json = localClient.getString(KEY_HISTORY_SEARCH, null)
            return gson.fromJson(json, object : TypeToken<LinkedList<Track>>() {}.type)
                ?: LinkedList()
        } else {
            return LinkedList()
        }
    }

    override fun setListToMemory() {
        val json = Gson().toJson(linkedList)
        if (localClient is SharedPreferences) {
            localClient.edit()
                .putString(KEY_HISTORY_SEARCH, json)
                .apply()
        }
    }

    companion object {
        private const val KEY_HISTORY_SEARCH = "KEY_HISTORY_SEARCH"
    }
}