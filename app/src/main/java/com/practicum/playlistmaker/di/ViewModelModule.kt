package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.presentation.view.PlayerViewModel
import com.practicum.playlistmaker.search.viewmodel.viewmodel.TrackSearchViewModel
import com.practicum.playlistmaker.setting.presentation.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TrackSearchViewModel(get(), get()) }
    viewModel { PlayerViewModel(get())}
    viewModel { ThemeViewModel(get()) }
}