package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.api.setting.ThemeSwitcherIteractor
import com.practicum.playlistmaker.domain.impl.setting.ThemeSwitcherIteractorImpl
import com.practicum.playlistmaker.presentation.root.SharedViewModel
import com.practicum.playlistmaker.presentation.setting.viewmodel.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory<ThemeSwitcherIteractor> { ThemeSwitcherIteractorImpl(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { SharedViewModel() }
}