package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode

class App : Application() {

  

import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    private lateinit var sharedPrefsTheme: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefsTheme = getSharedPreferences("THEME_APP", MODE_PRIVATE)

        darkTheme = sharedPrefsTheme.getBoolean(KEY_THEME_APP,
            getDefaultNightMode() != MODE_NIGHT_YES
        )
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefsTheme.edit()
            .putBoolean(KEY_THEME_APP, darkThemeEnabled)
            .apply()

        setDefaultNightMode(
            if (darkThemeEnabled) {
                MODE_NIGHT_YES
            } else {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO

            }
        )
    }
    companion object {
        const val THEME_APP = "THEME_APP"
        const val KEY_THEME_APP = "KEY_THEME_APP"
    }
}
