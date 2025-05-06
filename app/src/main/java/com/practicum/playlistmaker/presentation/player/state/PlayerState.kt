package com.practicum.playlistmaker.presentation.player.state

sealed class PlayerState {
    data class Default(
        val buttonState: Boolean = false,
        val progress: String = "00:00",
        val isFavourite: Boolean = false,
    ) : PlayerState()

    data class Prepared(
        val buttonState: Boolean = false,
        val progress: String = "00:00",
        val isFavourite: Boolean = false
    ) : PlayerState()

    data class Playing(
        val progress: String,
        val buttonState: Boolean = true,
        val isFavourite: Boolean = false
    ) : PlayerState()

    data class Paused(
        val progress: String,
        val buttonState: Boolean = false,
        val isFavourite: Boolean = false
    ) : PlayerState()
}