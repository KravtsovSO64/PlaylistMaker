package com.practicum.playlistmaker.searchMusic.creator

import android.app.Application
import com.practicum.playlistmaker.searchMusic.data.repositories.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.searchMusic.data.repositories.MusicLocalRepositoryImpl
import com.practicum.playlistmaker.searchMusic.data.repositories.MusicNetworkRepositoryImpl
import com.practicum.playlistmaker.searchMusic.data.repositories.local.SharedPrefsMusicStorage
import com.practicum.playlistmaker.searchMusic.data.repositories.network.RetrofitNetworkMusicStorage
import com.practicum.playlistmaker.searchMusic.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.searchMusic.domain.api.MusicNetworkInteractor
import com.practicum.playlistmaker.searchMusic.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicLocalRepository
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicNetworkRepository
import com.practicum.playlistmaker.searchMusic.domain.usecases.GetHistoryMusicUseCase
import com.practicum.playlistmaker.searchMusic.domain.usecases.GetMusicUseCase
import com.practicum.playlistmaker.searchMusic.domain.usecases.MediaPlayerIteratorImpl
import com.practicum.playlistmaker.searchMusic.domain.usecases.RemoveHistoryMusicUseCase
import com.practicum.playlistmaker.searchMusic.domain.usecases.SetHistoryMusicUseCase

object Creator {

    // Замените lateinit на обычное свойство, которое заполняется только при инициализации
    private lateinit var application: Application

    // Поставщик сетевого хранилища
    private fun getMusicNetworkRepository(): MusicNetworkRepository {
        return MusicNetworkRepositoryImpl(RetrofitNetworkMusicStorage())
    }

    // Установка Application
    fun initApplication(application: Application) {
        this.application = application
    }

    // Поставщик локального хранилища
    private fun getMusicLocalRepository(): MusicLocalRepository {
        return MusicLocalRepositoryImpl(SharedPrefsMusicStorage(context = application))
    }

    fun provideMusicInteractor(): MusicNetworkInteractor {
        return GetMusicUseCase(getMusicNetworkRepository())
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(isTrackFinished = false)
    }

    fun provideMediaPlayerInteractor(): MediaPlayerIterator {
        return MediaPlayerIteratorImpl(getMediaPlayerRepository())
    }


    // Используйте lazy для инициализации репозиториев только при необходимости
    val getHistory: GetHistoryMusicUseCase by lazy { GetHistoryMusicUseCase(getMusicLocalRepository()) }
    val setHistory: SetHistoryMusicUseCase by lazy { SetHistoryMusicUseCase(getMusicLocalRepository()) }
    val removeHistory: RemoveHistoryMusicUseCase by lazy { RemoveHistoryMusicUseCase(getMusicLocalRepository()) }

}
