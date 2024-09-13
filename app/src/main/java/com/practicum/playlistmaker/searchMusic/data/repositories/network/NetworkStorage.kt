package com.practicum.playlistmaker.searchMusic.data.repositories.network

import com.practicum.playlistmaker.searchMusic.data.dto.Response

fun interface NetworkStorage {
    fun doRequest(dto: Any): Response
}