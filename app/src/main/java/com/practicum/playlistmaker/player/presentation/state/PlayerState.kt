package com.practicum.playlistmaker.player.presentation.state

data class PlayerState (val isPlaying: Boolean = false,
                        val currentPosition: String = "00:00",
                        val audioUrl: String? = null)