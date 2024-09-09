package com.practicum.playlistmaker.searchMusic.data.repositories.network

import com.practicum.playlistmaker.MusicResponce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("/search?entity=song")
    fun getMusic(@Query("term") text: String): Call<MusicResponce>
}