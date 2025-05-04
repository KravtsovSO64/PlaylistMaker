package com.practicum.playlistmaker.presentation.player.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.player.state.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayerService: Service(), AudioPlayerControl {

    companion object {
        const val INTERVAL_TIMER = 200L
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "player_channel"
    }

    private val binder = PlayerServiceBinder()
    private var trackName: String = ""
    private var artistName: String = ""

    private var _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private val playerState = _playerState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var timerJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        val url = intent?.getStringExtra("TRACK_URL")
        if (url != null) {
            initMediaPlayer(url)
        }
        trackName = intent?.getStringExtra("TRACK_NAME").toString()
        artistName = intent?.getStringExtra("ARTIST_NAME").toString()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    private fun initMediaPlayer(url: String) {

        mediaPlayer?.apply {
            setDataSource(url)
            setOnPreparedListener {
                _playerState.value = PlayerState.Prepared()
            }
            setOnCompletionListener {
                timerJob?.cancel()
                timerJob = null
                _playerState.value = PlayerState.Default()
                stopForeground(NOTIFICATION_ID)
            }
            prepareAsync()
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        timerJob?.cancel()
        _playerState.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun getPlayerState(): StateFlow<PlayerState> = playerState

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer()
        createNotificationChannel()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    private fun getCurrentPlayerPosition(): String {
        val currentPosition = mediaPlayer?.currentPosition?.toLong() ?: 0L
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(currentPosition))
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(INTERVAL_TIMER)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
                Log.e("TIME", getCurrentPlayerPosition())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Player Service",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Playlist tMaker")
            .setContentText("$trackName - $artistName")
            .setSmallIcon(R.drawable.ic_app)
            .setOngoing(true)
            .build()

        if (mediaPlayer?.isPlaying == true) startForeground(NOTIFICATION_ID, notification)
    }

    override fun hideNotification() {
        stopForeground(NOTIFICATION_ID)
    }

    inner class PlayerServiceBinder(): Binder() {
        fun getPlayerService(): PlayerService = this@PlayerService
    }

}