package com.practicum.playlistmaker.domain.api.setting

interface ThemeSwitcherIteractor {
    fun switchTheme(isChecked: Boolean)
    fun isDarkThemeEnabled(): Boolean
}