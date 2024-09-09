package com.practicum.playlistmaker.searchMusic

import com.practicum.playlistmaker.searchMusic.data.TrackRepositoryImpl
import com.practicum.playlistmaker.searchMusic.data.repositories.local.SharedPrefClient
import com.practicum.playlistmaker.searchMusic.data.repositories.network.RetrofitNetworkClient
import com.practicum.playlistmaker.searchMusic.domain.api.HistoryRepository
import com.practicum.playlistmaker.searchMusic.domain.api.MusicInteractor
import com.practicum.playlistmaker.searchMusic.domain.api.MusicRepository
import com.practicum.playlistmaker.searchMusic.domain.use_cases.HistoryRepositoryImpl
import com.practicum.playlistmaker.searchMusic.domain.use_cases.MusicInteractorImpl

object Creator {
    private fun getMusicRepository(): MusicRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun getHistorySearchMusic(): HistoryRepository {
        return HistoryRepositoryImpl(SharedPrefClient())
    }

    fun provideMusicInteractor(): MusicInteractor {
        return MusicInteractorImpl(getMusicRepository())
    }

}