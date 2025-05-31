package com.practicum.playlistmaker.presentation

import com.practicum.playlistmaker.domain.model.Track

fun interface OnTrackClickListener {
    fun onItemClick(track: Track)
}


