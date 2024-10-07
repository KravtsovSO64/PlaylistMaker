package com.practicum.playlistmaker.settingApp.domain.iterator

import com.practicum.playlistmaker.settingApp.data.repositories.ThemePreferenceRepositoryImpl
import com.practicum.playlistmaker.settingApp.domain.api.ThemeSwitcherIteractor

class ThemeSwitcherInteractorImpl(private val themePreferenceRepository: ThemePreferenceRepositoryImpl)
    : ThemeSwitcherIteractor {
    override fun switchTheme(isChecked: Boolean) {
        themePreferenceRepository.setDarkThemeEnabled(isChecked)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return themePreferenceRepository.isDarkThemeEnabled()
    }
}