package com.practicum.playlistmaker.setting.creator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.setting.domain.usecases.ViewModelFactory
import com.practicum.playlistmaker.setting.presentation.ThemeViewModel



class App : Application() {
    private val appViewModelStore = ViewModelStore()
    private val viewModelFactory = ViewModelFactory()

    val themeViewModel: ThemeViewModel by lazy {
        ViewModelProvider(appViewModelStore, viewModelFactory).get(ThemeViewModel::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        val themePreferenceRepository = Creator.provideThemePreferenceIterator()

        // Установка темы при запуске
        themeViewModel.isDarkThemeEnabled.observeForever { isDarkTheme ->
            switchTheme(isDarkTheme)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val mode = if (darkThemeEnabled) {
            MODE_NIGHT_YES
        } else {
            MODE_NIGHT_NO
        }
        setDefaultNightMode(mode)
    }
}