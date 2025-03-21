package com.practicum.playlistmaker.data.repositories.search.network

import com.practicum.playlistmaker.data.dto.search.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
