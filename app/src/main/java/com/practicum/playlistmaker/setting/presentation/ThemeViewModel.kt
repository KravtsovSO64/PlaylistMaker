package com.practicum.playlistmaker.setting.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator

class ThemeViewModel() : ViewModel() {

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>()
    val isDarkThemeEnabled: LiveData<Boolean> = _isDarkThemeEnabled

    val iterator = Creator.provideThemePreferenceIterator()

    init {
        _isDarkThemeEnabled.value = iterator.isDarkThemeEnabled()
    }

    fun switchTheme(isChecked: Boolean) {
        iterator.switchTheme(isChecked)
        _isDarkThemeEnabled.value = isChecked
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    ThemeViewModel()
                }
            }
        }
    }

}