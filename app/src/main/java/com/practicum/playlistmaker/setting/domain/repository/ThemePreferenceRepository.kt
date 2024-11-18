package com.practicum.playlistmaker.setting.domain.repository

interface ThemePreferenceRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(enabled: Boolean)
}