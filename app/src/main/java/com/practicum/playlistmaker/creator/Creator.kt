package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.usecase.MediaPlayerIteratorImpl
import com.practicum.playlistmaker.search.data.repositories.MusicLocalRepositoryImpl
import com.practicum.playlistmaker.search.data.repositories.MusicNetworkRepositoryImpl
import com.practicum.playlistmaker.search.data.repositories.local.SharedPrefsMusicStorage
import com.practicum.playlistmaker.search.domain.api.MusicLocalIterator
import com.practicum.playlistmaker.search.domain.api.MusicNetworkInteractor
import com.practicum.playlistmaker.search.domain.repository.MusicLocalRepository
import com.practicum.playlistmaker.search.domain.repository.MusicNetworkRepository
import com.practicum.playlistmaker.search.domain.usecases.MusicLocalIteratorImpl
import com.practicum.playlistmaker.search.domain.usecases.MusicNetworkInteractorImpl
import com.practicum.playlistmaker.setting.data.repositories.ThemePreferenceRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.ThemeSwitcherIteractor
import com.practicum.playlistmaker.setting.domain.repository.ThemePreferenceRepository
import com.practicum.playlistmaker.setting.domain.usecases.ThemeSwitcherIteractorImpl

object Creator {

    // Замените lateinit на обычное свойство, которое заполняется только при инициализации
    private lateinit var application: Application
    const val SEARCH_REQUEST = "SEARCH_REQUEST"
    const val AMOUNT_DEF = ""
    const val TRACK = "track"

    fun initApplication(application: Application) {
        Creator.application = application
    }

    private fun getMusicNetworkRepository(): MusicNetworkRepository {
        return MusicNetworkRepositoryImpl()
    }

    private fun getMusicLocalRepository(): MusicLocalRepository {
        return MusicLocalRepositoryImpl(SharedPrefsMusicStorage(context = application))
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    private fun getThemePreferenceRepository(): ThemePreferenceRepository {
        return ThemePreferenceRepositoryImpl(context = application)
    }

    fun provideMusicLocalIterator(): MusicLocalIterator {
        return MusicLocalIteratorImpl(getMusicLocalRepository())
    }

    fun provideMusicInteractor(): MusicNetworkInteractor {
        return MusicNetworkInteractorImpl(getMusicNetworkRepository())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerIterator {
        return MediaPlayerIteratorImpl(getMediaPlayerRepository())
    }

    fun provideThemePreferenceIterator(): ThemeSwitcherIteractor {
        return ThemeSwitcherIteractorImpl(getThemePreferenceRepository())
    }

}
