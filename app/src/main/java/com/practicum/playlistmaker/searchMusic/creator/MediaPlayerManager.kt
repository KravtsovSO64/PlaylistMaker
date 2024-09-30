package com.practicum.playlistmaker.searchMusic.creator

import android.media.MediaPlayer

class MediaPlayerManager(private val onPlaybackStateChange: (Boolean) -> Unit) {
    private val mediaPlayer = MediaPlayer()

    fun prepareMedia(previewUrl: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPlaybackStateChange(true) // Воспроизведение возможно
        }
        mediaPlayer.setOnCompletionListener {
            onPlaybackStateChange(false) // Завершено
        }
    }

    fun startPlayback() {
        mediaPlayer.start()
    }

    fun pausePlayback() {
        mediaPlayer.pause()
    }

    fun release() {
        mediaPlayer.release()
    }

    val currentPosition: Int
        get() = mediaPlayer.currentPosition
}
