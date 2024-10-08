package com.practicum.playlistmaker.settingApp.domain.iterator

import com.practicum.playlistmaker.settingApp.domain.api.ThemeSwitcherIteractor
import com.practicum.playlistmaker.settingApp.domain.repository.ThemePreferenceRepository

class ThemeSwitcherIteractorImpl(private val themePreferenceRepository: ThemePreferenceRepository)
    : ThemeSwitcherIteractor {
    override fun switchTheme(isChecked: Boolean) {
        themePreferenceRepository.setDarkThemeEnabled(isChecked)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return themePreferenceRepository.isDarkThemeEnabled()
    }
}