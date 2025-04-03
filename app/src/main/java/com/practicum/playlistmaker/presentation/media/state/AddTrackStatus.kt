package com.practicum.playlistmaker.presentation.media.state

sealed class AddTrackStatus {
    data class Success(val namePlaylist: String) : AddTrackStatus()
    data class AlreadyExists(val namePlaylist: String) : AddTrackStatus()
}