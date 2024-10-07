package com.practicum.playlistmaker.settingApp.data

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.practicum.playlistmaker.settingApp.data.repositories.ThemePreferenceRepositoryImpl
import kotlin.properties.Delegates


class App : Application() {
    private var darkTheme by Delegates.notNull<Boolean>()
    private lateinit var sharedPrefsTheme: ThemePreferenceRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        sharedPrefsTheme = ThemePreferenceRepositoryImpl(this)

        darkTheme = sharedPrefsTheme.isDarkThemeEnabled()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefsTheme.setDarkThemeEnabled(darkThemeEnabled)

        if (darkThemeEnabled) {
            setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            setDefaultNightMode(MODE_NIGHT_NO)
        }
    }
}



