package com.practicum.playlistmaker.search.data.repositories.network

import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("/search?entity=song")
    fun searchMusic(@Query("term") text: String): Call<TrackSearchResponse>
}