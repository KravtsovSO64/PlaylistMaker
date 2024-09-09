package com.practicum.playlistmaker.searchMusic.data.repositories.local

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.searchMusic.data.LocalClient

class SharedPrefClient: LocalClient, AppCompatActivity() {
    override fun getMusicLocal(): SharedPreferences {
        val sharedPrefs = getSharedPreferences(HISTORY_SEARCH, MODE_APPEND)
        return sharedPrefs
    }

    companion object {
        private const val HISTORY_SEARCH = "history_search"
    }
}