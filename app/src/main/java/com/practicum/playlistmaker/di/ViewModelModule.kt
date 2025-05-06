package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.media.viewmodel.FavoriteTracksViewModel
import com.practicum.playlistmaker.presentation.media.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.presentation.player.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.presentation.search.viewmodel.TrackSearchViewModel
import com.practicum.playlistmaker.presentation.setting.viewmodel.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TrackSearchViewModel(get(), get(), get()) }
    viewModel { PlayerViewModel(get(), get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { FavoriteTracksViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }

}