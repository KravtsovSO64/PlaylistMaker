package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.player.domain.usecase.MediaPlayerIteratorImpl
import com.practicum.playlistmaker.search.domain.api.MusicLocalIterator
import com.practicum.playlistmaker.search.domain.api.MusicNetworkInteractor
import com.practicum.playlistmaker.search.domain.usecases.MusicLocalIteratorImpl
import com.practicum.playlistmaker.search.domain.usecases.MusicNetworkInteractorImpl
import com.practicum.playlistmaker.setting.domain.api.ThemeSwitcherIteractor
import com.practicum.playlistmaker.setting.domain.usecases.ThemeSwitcherIteractorImpl
import org.koin.dsl.module

val iteratorModule = module {
    factory<MusicLocalIterator> { MusicLocalIteratorImpl(get()) }
    factory<MusicNetworkInteractor> { MusicNetworkInteractorImpl(get()) }
    factory<MediaPlayerIterator> { MediaPlayerIteratorImpl(get()) }
    factory<ThemeSwitcherIteractor> { ThemeSwitcherIteractorImpl(get())  }
}