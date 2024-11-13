package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerStatusListener
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private var listener: PlayerStatusListener? = null) : MediaPlayerRepository {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentAudioUrl: String? = null
    private var isTrackFinished: Boolean = false


    init {
        mediaPlayer.setOnCompletionListener {
            isTrackFinished = true
        }
        mediaPlayer.setOnErrorListener { _, _, _ ->
            true
        }
    }


    override fun play(audioUrl: String) {
        if (currentAudioUrl != audioUrl) {
            currentAudioUrl = audioUrl
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.setOnCompletionListener { listener?.onPlaybackCompleted() }
            mediaPlayer.prepare()
        }
        mediaPlayer.start()
    }

    override fun pause() {
        if (isPlaying()) {
            mediaPlayer.pause()
        }
    }

    override fun stop() {
        if (isPlaying()) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            currentAudioUrl = null
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun resume() {
        if (!isPlaying() && currentAudioUrl != null) {
            mediaPlayer.start()
        }
    }

    override fun setPlayerStatusListener(statusListener: PlayerStatusListener) {
        this.listener = statusListener
    }
}