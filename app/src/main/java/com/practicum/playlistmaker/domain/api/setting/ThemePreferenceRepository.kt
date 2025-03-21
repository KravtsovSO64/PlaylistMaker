package com.practicum.playlistmaker.domain.api.setting

interface ThemePreferenceRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(enabled: Boolean)
}