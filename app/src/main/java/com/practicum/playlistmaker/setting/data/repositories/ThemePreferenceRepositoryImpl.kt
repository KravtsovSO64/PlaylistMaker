package com.practicum.playlistmaker.setting.data.repositories

import android.content.Context
import com.practicum.playlistmaker.setting.domain.repository.ThemePreferenceRepository

class ThemePreferenceRepositoryImpl(context: Context): ThemePreferenceRepository {
    private val sharedPreferences = context.getSharedPreferences(THEME_APP, Context.MODE_PRIVATE)


    //получаем информацию включена ли ночная тема из префа
    override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_THEME_APP, false)
    }

    //включаем ночную тему
    override fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_THEME_APP, enabled).apply()
    }

    companion object {
        const val THEME_APP = "THEME_APP"
        const val KEY_THEME_APP = "KEY_THEME_APP"
    }
}
