package com.practicum.playlistmaker.searchMusic.data

import com.practicum.playlistmaker.searchMusic.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}