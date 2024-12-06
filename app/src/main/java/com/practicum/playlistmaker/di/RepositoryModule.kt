package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.search.data.repositories.MusicLocalRepositoryImpl
import com.practicum.playlistmaker.search.data.repositories.MusicNetworkRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.MusicLocalRepository
import com.practicum.playlistmaker.search.domain.repository.MusicNetworkRepository
import com.practicum.playlistmaker.setting.data.repositories.ThemePreferenceRepositoryImpl
import com.practicum.playlistmaker.setting.domain.repository.ThemePreferenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<MusicLocalRepository> { MusicLocalRepositoryImpl(get()) }
    single<MusicNetworkRepository> { MusicNetworkRepositoryImpl(get())  }
    single<MediaPlayerRepository> { MediaPlayerRepositoryImpl(null, get()) }
    single<ThemePreferenceRepository> { ThemePreferenceRepositoryImpl(androidContext()) }
}