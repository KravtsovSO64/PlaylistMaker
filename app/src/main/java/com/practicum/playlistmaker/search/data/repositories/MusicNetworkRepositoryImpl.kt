package com.practicum.playlistmaker.search.data.repositories

import com.practicum.playlistmaker.search.data.dto.Result
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.repositories.mapper.MapperTrackFromTrackDto
import com.practicum.playlistmaker.search.data.repositories.network.MusicApiService
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.MusicNetworkRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MusicNetworkRepositoryImpl(private val trackService: MusicApiService) : MusicNetworkRepository {


    override fun searchMusic(expression: String, callback: (Result<List<Track>>) -> Unit) {
        // Выполняем сетевой запрос
        trackService.searchMusic(expression).enqueue(object : Callback<TrackSearchResponse> {
            override fun onResponse(call: Call<TrackSearchResponse>, response: Response<TrackSearchResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val resultList = response.body()!!.results
                    val tracks = resultList.map { MapperTrackFromTrackDto().execute(it) } ?: emptyList()
                    callback(Result.Success(tracks, response.code()))
                } else {
                    callback(Result.Failure(response.code()))
                }
            }

            override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                callback(Result.Failure(-1))
            }
        })
    }
}


