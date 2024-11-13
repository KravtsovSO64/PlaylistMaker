package com.practicum.playlistmaker.creator

import android.app.Application
import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.usecase.MediaPlayerIteratorImpl
import com.practicum.playlistmaker.search.data.repositories.MusicLocalRepositoryImpl
import com.practicum.playlistmaker.search.data.repositories.MusicNetworkRepositoryImpl
import com.practicum.playlistmaker.search.data.repositories.local.SharedPrefsMusicStorage
import com.practicum.playlistmaker.search.data.repositories.network.MusicApiService
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private lateinit var application: Application
    const val SEARCH_REQUEST = "SEARCH_REQUEST"
    const val AMOUNT_DEF = ""
    const val TRACK = "track"

    fun initApplication(application: Application) {
        Creator.application = application
    }

    private fun getMusicNetworkRepository(): MusicNetworkRepository {
        return MusicNetworkRepositoryImpl(getNetworkClient())
    }

    private fun getMusicLocalRepository(): MusicLocalRepository {
        return MusicLocalRepositoryImpl(SharedPrefsMusicStorage(context = application))
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(null , getMediaPlayer())
    }

    private fun getThemePreferenceRepository(): ThemePreferenceRepository {
        return ThemePreferenceRepositoryImpl(context = application)
    }

    private fun getNetworkClient(): MusicApiService{
        val baseUrl = "https://itunes.apple.com"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MusicApiService::class.java)
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
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
