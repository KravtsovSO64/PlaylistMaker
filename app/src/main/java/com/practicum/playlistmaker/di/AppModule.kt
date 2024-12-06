package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.setting.domain.api.ThemeSwitcherIteractor
import com.practicum.playlistmaker.setting.domain.usecases.ThemeSwitcherIteractorImpl
import com.practicum.playlistmaker.setting.presentation.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ThemeSwitcherIteractor> { ThemeSwitcherIteractorImpl(get()) }
    viewModel { ThemeViewModel(get()) }
}