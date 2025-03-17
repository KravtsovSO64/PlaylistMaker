package com.practicum.playlistmaker.presentation.root

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.model.Track

class SharedViewModel: ViewModel() {
    private val _items: MutableLiveData<List<Track>> = MutableLiveData()
    val items: LiveData<List<Track>> get() = _items

    fun setItems(list: List<Track>) {
        _items.value = list
    }

    fun removeItems() {
        _items.value = emptyList()
    }
}
