package com.practicum.playlistmaker.searchMusic.data.repositories.network

import android.util.Log
import com.practicum.playlistmaker.searchMusic.data.dto.Response
import com.practicum.playlistmaker.searchMusic.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitNetworkMusicStorage: NetworkStorage {
    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(MusicApiService::class.java)


    //Выполняем запрос и получаем объект ответа Response
    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            val resp = trackService.searchMusic(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }

        Log.d("Error 4", "Response received: ${Response().resultCode}") // Логируем ответ
    }
}
