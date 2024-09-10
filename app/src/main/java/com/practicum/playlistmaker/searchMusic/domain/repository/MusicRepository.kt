package com.practicum.playlistmaker.searchMusic.domain.repository

import com.practicum.playlistmaker.searchMusic.domain.models.Track

interface MusicRepository {
    fun searchMusic(expression: String): List<Track>

}