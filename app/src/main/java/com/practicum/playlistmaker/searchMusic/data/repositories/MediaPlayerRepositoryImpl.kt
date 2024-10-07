package com.practicum.playlistmaker.searchMusic.data.repositories

import android.media.MediaPlayer
import com.practicum.playlistmaker.searchMusic.domain.api.PlayerStatus
import com.practicum.playlistmaker.searchMusic.domain.api.PlayerStatusListener
import com.practicum.playlistmaker.searchMusic.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(var isTrackFinished: Boolean) : MediaPlayerRepository {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentAudioUrl: String? = null
    private var playerStatusListener: PlayerStatusListener? = null


    init {
        mediaPlayer.setOnCompletionListener {
            isTrackFinished = true
            playerStatusListener?.onPlayerStatusChanged(PlayerStatus.COMPLETED)
        }

        mediaPlayer.setOnPreparedListener {
            playerStatusListener?.onPlayerStatusChanged(PlayerStatus.PREPARING)
            mediaPlayer.start()
            playerStatusListener?.onPlayerStatusChanged(PlayerStatus.PLAYING)
        }

        mediaPlayer.setOnErrorListener { _, _, _ ->
            playerStatusListener?.onPlayerStatusChanged(PlayerStatus.ERROR)
            true
        }
    }

    override fun setPlayerStatusListener(listener: PlayerStatusListener) {
        playerStatusListener = listener
    }

    override fun play(audioUrl: String) {
        if (currentAudioUrl != audioUrl) {
            currentAudioUrl = audioUrl
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepare()
        }
        mediaPlayer.start()
    }

    override fun pause() {
        if (isPlaying()) {
            mediaPlayer.pause()
            playerStatusListener?.onPlayerStatusChanged(PlayerStatus.PAUSED)
        }
    }

    override fun stop() {
        if (isPlaying()) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            currentAudioUrl = null
            playerStatusListener?.onPlayerStatusChanged(PlayerStatus.STOPPED)
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
}