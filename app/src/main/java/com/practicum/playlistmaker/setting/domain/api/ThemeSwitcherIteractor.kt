package com.practicum.playlistmaker.setting.domain.api

interface ThemeSwitcherIteractor {
    fun switchTheme(isChecked: Boolean)
    fun isDarkThemeEnabled(): Boolean
}