package com.practicum.playlistmaker.searchMusic.domain.api

import com.practicum.playlistmaker.searchMusic.domain.entities.Track

interface MusicRepository {
    fun searchMusic(expression: String): List<Track>
}