package com.practicum.playlistmaker.presentation.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.api.setting.ThemeSwitcherIteractor

class ThemeViewModel(private val iterator: ThemeSwitcherIteractor) : ViewModel() {

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>()
    val isDarkThemeEnabled: LiveData<Boolean> = _isDarkThemeEnabled

    init {
        _isDarkThemeEnabled.value = iterator.isDarkThemeEnabled()
    }

    fun switchTheme(isChecked: Boolean) {
        iterator.switchTheme(isChecked)
        _isDarkThemeEnabled.value = isChecked
    }
}
