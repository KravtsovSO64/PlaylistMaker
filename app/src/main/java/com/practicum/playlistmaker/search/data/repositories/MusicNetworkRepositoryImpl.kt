package com.practicum.playlistmaker.search.data.repositories

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.repositories.mapper.MapperTrackFromTrackDto
import com.practicum.playlistmaker.search.data.repositories.network.NetworkClient
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.MusicNetworkRepository
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MusicNetworkRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context,
    private val mapper: MapperTrackFromTrackDto) : MusicNetworkRepository {
    override fun searchMusic(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when(response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.checkInternetConnection)))
            }
            200 -> {
                with(response as TrackSearchResponse) {
                    emit(Resource.Success(results.map { trackDto -> mapper.execute(trackDto) }))
                }
            }
            else -> {
                emit(Resource.Error(context.getString(R.string.errorServer)))
            }
        }
    }
}


