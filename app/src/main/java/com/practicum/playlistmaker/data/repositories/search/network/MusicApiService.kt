package com.practicum.playlistmaker.data.repositories.search.network

import com.practicum.playlistmaker.data.dto.search.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("/search?entity=song")
    suspend fun searchMusic(@Query("term") text: String): TrackSearchResponse
}