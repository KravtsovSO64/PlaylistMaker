package com.practicum.playlistmaker.presentation.player.state

data class PlayerState (val isPlaying: Boolean = false,
                        val currentPosition: String = "00:00",
                        val audioUrl: String? = null,
                        val isFavourite: Boolean = false)