package com.practicum.playlistmaker.setting.domain.usecases

import com.practicum.playlistmaker.setting.domain.api.ThemeSwitcherIteractor
import com.practicum.playlistmaker.setting.domain.repository.ThemePreferenceRepository

class ThemeSwitcherIteractorImpl(private val themePreferenceRepository: ThemePreferenceRepository)
    : ThemeSwitcherIteractor {
    override fun switchTheme(isChecked: Boolean) {
        themePreferenceRepository.setDarkThemeEnabled(isChecked)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return themePreferenceRepository.isDarkThemeEnabled()
    }
}