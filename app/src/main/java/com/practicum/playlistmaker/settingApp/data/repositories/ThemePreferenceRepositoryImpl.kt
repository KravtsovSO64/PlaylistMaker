package com.practicum.playlistmaker.settingApp.data.repositories

import android.content.Context
import com.practicum.playlistmaker.settingApp.domain.repository.ThemePreferenceRepository

class ThemePreferenceRepositoryImpl(context: Context): ThemePreferenceRepository {
    private val sharedPreferences = context.getSharedPreferences(THEME_APP, Context.MODE_PRIVATE)

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_THEME_APP, false)
    }

    override fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_THEME_APP, enabled).apply()
    }

    companion object {
        const val THEME_APP = "THEME_APP"
        const val KEY_THEME_APP = "KEY_THEME_APP"
    }
}
