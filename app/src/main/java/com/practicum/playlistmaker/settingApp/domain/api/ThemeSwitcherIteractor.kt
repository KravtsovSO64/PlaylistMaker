package com.practicum.playlistmaker.settingApp.domain.api

interface ThemeSwitcherIteractor {
    fun switchTheme(isChecked: Boolean)
    fun isDarkThemeEnabled(): Boolean
}