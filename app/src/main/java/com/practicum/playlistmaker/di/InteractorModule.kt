package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.api.media.FavouriteTrackIterator
import com.practicum.playlistmaker.domain.api.player.MediaPlayerIterator
import com.practicum.playlistmaker.domain.api.search.MusicLocalIterator
import com.practicum.playlistmaker.domain.api.search.MusicNetworkIterator
import com.practicum.playlistmaker.domain.api.setting.ThemeSwitcherIteractor
import com.practicum.playlistmaker.domain.impl.media.FavouriteTrackIteratorImpl
import com.practicum.playlistmaker.domain.impl.player.MediaPlayerIteratorImpl
import com.practicum.playlistmaker.domain.impl.search.MusicLocalIteratorImpl
import com.practicum.playlistmaker.domain.impl.search.MusicNetworkIteratorImpl
import com.practicum.playlistmaker.domain.impl.setting.ThemeSwitcherIteractorImpl
import org.koin.dsl.module

val iteratorModule = module {
    factory<MusicLocalIterator> { MusicLocalIteratorImpl(get(), get()) }
    factory<MusicNetworkIterator> { MusicNetworkIteratorImpl(get(), get()) }
    factory<MediaPlayerIterator> { MediaPlayerIteratorImpl(get()) }
    factory<ThemeSwitcherIteractor> { ThemeSwitcherIteractorImpl(get())  }
    factory<FavouriteTrackIterator> { FavouriteTrackIteratorImpl(get()) }
}