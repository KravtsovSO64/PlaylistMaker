package com.practicum.playlistmaker.presentation

import com.practicum.playlistmaker.domain.model.Track

interface OnTrackLongClickListener {
    fun onItemLongClick(track: Track): Boolean
}