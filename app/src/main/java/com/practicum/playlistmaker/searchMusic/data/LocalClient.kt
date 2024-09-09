package com.practicum.playlistmaker.searchMusic.data

import android.content.SharedPreferences

interface LocalClient {
    fun getMusicLocal(): SharedPreferences
}