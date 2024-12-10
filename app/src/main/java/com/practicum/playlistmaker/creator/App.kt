package com.practicum.playlistmaker.creator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.appModule
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.iteratorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.setting.domain.repository.ThemePreferenceRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule, dataModule, repositoryModule, iteratorModule, viewModelModule)
        }

        val themePreferenceRepository: ThemePreferenceRepository by inject()

        val isDarkTheme = themePreferenceRepository.isDarkThemeEnabled()
        switchTheme(isDarkTheme)
    }

    private fun switchTheme(isDarkTheme: Boolean) {
        val mode = if (isDarkTheme)  AppCompatDelegate.MODE_NIGHT_YES else  AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
