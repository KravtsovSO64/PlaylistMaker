package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApi {
    @GET("/search?entity=song")
    fun getMusic(@Query("term") text: String): Call<MusicResponce>
}