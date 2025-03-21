package com.practicum.playlistmaker.domain.impl.setting

import com.practicum.playlistmaker.domain.api.setting.ThemeSwitcherIteractor
import com.practicum.playlistmaker.domain.api.setting.ThemePreferenceRepository

class ThemeSwitcherIteractorImpl(private val themePreferenceRepository: ThemePreferenceRepository)
    : ThemeSwitcherIteractor {
    override fun switchTheme(isChecked: Boolean) {
        themePreferenceRepository.setDarkThemeEnabled(isChecked)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return themePreferenceRepository.isDarkThemeEnabled()
    }
}