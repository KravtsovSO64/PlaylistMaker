package com.practicum.playlistmaker.searchMusic.data.repositories.network

import com.practicum.playlistmaker.searchMusic.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}