package com.practicum.playlistmaker.searchMusic.creator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.practicum.playlistmaker.settingApp.domain.api.ThemeSwitcherIteractor
import kotlin.properties.Delegates


class App : Application() {
    private var darkTheme by Delegates.notNull<Boolean>()
    private lateinit var sharedPrefsTheme: ThemeSwitcherIteractor

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        sharedPrefsTheme = Creator.provideThemePreferenceIterator()

        Creator.initApplication(this)

        darkTheme = sharedPrefsTheme.isDarkThemeEnabled()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefsTheme.switchTheme(darkThemeEnabled)

        if (darkThemeEnabled) {
            setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            setDefaultNightMode(MODE_NIGHT_NO)
        }
    }
}



