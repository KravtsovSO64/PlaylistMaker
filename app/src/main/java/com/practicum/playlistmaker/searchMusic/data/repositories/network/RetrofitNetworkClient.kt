package com.practicum.playlistmaker.searchMusic.data.repositories.network

import com.practicum.playlistmaker.searchMusic.data.dto.Response
import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitNetworkClient: NetworkClient {
    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(MusicApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp  = trackService.getMusic(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply {resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}