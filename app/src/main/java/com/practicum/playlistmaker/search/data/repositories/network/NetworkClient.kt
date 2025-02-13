package com.practicum.playlistmaker.search.data.repositories.network

import com.practicum.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
