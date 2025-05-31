package com.practicum.playlistmaker.presentation.setting.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.setting.ThemeSwitcherIteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class ThemeViewModel(
    private val iterator: ThemeSwitcherIteractor
) : ViewModel() {

    private val _isDarkThemeEnabled = MutableStateFlow(iterator.isDarkThemeEnabled())
    val isDarkThemeEnabled: StateFlow<Boolean> = _isDarkThemeEnabled.asStateFlow()

    fun switchTheme(isChecked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            })

        viewModelScope.launch {
            iterator.switchTheme(isChecked)
            _isDarkThemeEnabled.value = isChecked
        }
    }
}