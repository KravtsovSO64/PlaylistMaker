package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.search.data.repositories.MusicLocalRepositoryImpl
import com.practicum.playlistmaker.search.data.repositories.MusicNetworkRepositoryImpl
import com.practicum.playlistmaker.search.data.repositories.mapper.MapperTrackFromTrackDto
import com.practicum.playlistmaker.search.domain.api.MusicLocalRepository
import com.practicum.playlistmaker.search.domain.api.MusicNetworkRepository
import com.practicum.playlistmaker.setting.data.repositories.ThemePreferenceRepositoryImpl
import com.practicum.playlistmaker.setting.domain.repository.ThemePreferenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory { MapperTrackFromTrackDto() }
    factory<MusicLocalRepository> { MusicLocalRepositoryImpl(get()) }
    factory<MusicNetworkRepository> { MusicNetworkRepositoryImpl(get(), androidContext(), get())  }
    factory<MediaPlayerRepository> { MediaPlayerRepositoryImpl(null, get()) }
    factory<ThemePreferenceRepository> { ThemePreferenceRepositoryImpl(androidContext()) }
}