package com.practicum.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.LinkedList
class HistorySearch(private val sharedPreferences: SharedPreferences) : AppCompatActivity() {

    var linkedList = getListFromMemory(sharedPreferences)
    private val maxSizeList: Int = 10


    fun getListHistory(): ArrayList<Track> {
        return ArrayList(linkedList)
    }

    fun addTrackToListHistory(track : Track){
            val iterator = linkedList.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item.trackId == track.trackId) {
                    iterator.remove()
                    break
                }
            }
        if (linkedList.size < maxSizeList){
            linkedList.addFirst(track)
        } else {
            linkedList.removeLast()
            linkedList.addFirst(track)
        }
    }


    fun clearTracksToListHistory(){
        linkedList.clear()
    }

    fun getListFromMemory(sharedPreferences: SharedPreferences) : LinkedList<Track> {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_HISTORY_SEARCH, null)
        return gson.fromJson(json, object : TypeToken<LinkedList<Track>>() {}.type) ?: LinkedList<Track>()
    }

    fun setListToMemory(sharedPreferences: SharedPreferences) {
        val json = Gson().toJson(linkedList)
        sharedPreferences.edit()
            .putString(KEY_HISTORY_SEARCH, json)
            .apply()
    }

    companion object{
        private const val HISTORY_SEARCH = "history_search"
        private const val KEY_HISTORY_SEARCH = "KEY_HISTORY_SEARCH"
    }

}