package com.practicum.playlistmaker.searchMusic.creator

import com.practicum.playlistmaker.searchMusic.data.repositories.network.MusicRepositoryImpl
import com.practicum.playlistmaker.searchMusic.data.repositories.network.RetrofitNetworkClient
import com.practicum.playlistmaker.searchMusic.domain.api.MusicInteractor
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicRepository
import com.practicum.playlistmaker.searchMusic.domain.usecases.GetMusicUseCase

object Creator {
    private fun getMusicRepository(): MusicRepository {
        return MusicRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMusicInteractor(): MusicInteractor {
        return GetMusicUseCase(getMusicRepository())
    }

}