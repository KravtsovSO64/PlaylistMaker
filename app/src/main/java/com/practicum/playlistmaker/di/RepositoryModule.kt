package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.repositories.media.FavouriteTrackRepositoryImpl
import com.practicum.playlistmaker.data.repositories.player.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.repositories.search.MusicLocalRepositoryImpl
import com.practicum.playlistmaker.data.repositories.search.MusicNetworkRepositoryImpl
import com.practicum.playlistmaker.data.repositories.search.converter.TrackConverter
import com.practicum.playlistmaker.data.repositories.setting.ThemePreferenceRepositoryImpl
import com.practicum.playlistmaker.domain.api.media.FavouriteTrackRepository
import com.practicum.playlistmaker.domain.api.player.MediaPlayerRepository
import com.practicum.playlistmaker.domain.api.search.MusicLocalRepository
import com.practicum.playlistmaker.domain.api.search.MusicNetworkRepository
import com.practicum.playlistmaker.domain.api.setting.ThemePreferenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory { TrackConverter() }
    factory<MusicLocalRepository> { MusicLocalRepositoryImpl(get(), get()) }
    factory<MusicNetworkRepository> { MusicNetworkRepositoryImpl(get(), androidContext(), get())  }
    factory<MediaPlayerRepository> { MediaPlayerRepositoryImpl(null, get()) }
    factory<ThemePreferenceRepository> { ThemePreferenceRepositoryImpl(androidContext()) }
    factory<FavouriteTrackRepository> { FavouriteTrackRepositoryImpl(get(), get()) }
}