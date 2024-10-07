package com.practicum.playlistmaker.settingApp.domain.repository

interface ThemePreferenceRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(enabled: Boolean)
}