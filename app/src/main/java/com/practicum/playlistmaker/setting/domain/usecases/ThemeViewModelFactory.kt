package com.practicum.playlistmaker.setting.domain.usecases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.practicum.playlistmaker.setting.presentation.ThemeViewModel


class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemeViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}