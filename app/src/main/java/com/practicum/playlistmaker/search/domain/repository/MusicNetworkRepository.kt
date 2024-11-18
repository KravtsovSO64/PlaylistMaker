package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.data.dto.Result
import com.practicum.playlistmaker.search.domain.models.Track

interface MusicNetworkRepository {

    fun searchMusic(expression: String, callback: (Result<List<Track>>) -> Unit)

}