package com.practicum.playlistmaker.searchMusic.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.searchMusic.creator.MediaPlayerManager
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.domain.usecases.MediaPlayerUseCase
import com.practicum.playlistmaker.searchMusic.presentation.uiComponents.PlayerPresenter
import java.time.ZonedDateTime

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var play: ImageView
    private lateinit var presenter: PlayerPresenter
    private lateinit var mediaPlayerManager: MediaPlayerManager
    private lateinit var mainThreadHandler: Handler
    private lateinit var timer: TextView

    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()
        setupUI()
        configureMediaPlayer()
    }

    private fun initializeComponents() {
        mainThreadHandler = Handler(Looper.getMainLooper())
        timer = binding.trackElapsedTimePlayer

        // Получение трека из Intent
        track = intent.getParcelableExtra("track")
            ?: throw IllegalArgumentException("Track data required")

        play = binding.buttonPlayStopPlayer
        mediaPlayerManager = MediaPlayerManager { isPlayable ->
            updatePlaybackState(isPlayable)
        }
        presenter = PlayerPresenter(MediaPlayerUseCase(mediaPlayerManager))
    }

    private fun setupUI() {
        binding.trackNamePlayer.text = track.trackName
        binding.artistNamePlayer.text = track.artistName
        binding.durationTrackPlayer.text = track.trackTimeMillis.toString()
        binding.albumTrackPlayer.text = track.collectionName
        binding.releaseYearTrackPlayer.text = track.releaseDate?.let { getYearFromDate(it).toString() }
        binding.styleTrackPlayer.text = track.primaryGenreName
        binding.countryTrackPlayer.text = track.country

        Glide.with(this)
            .load(getCoverArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.placeHolderPlayer)

        play.setImageResource(R.drawable.ic_button_play)

        binding.arrowBackPlayer.setNavigationOnClickListener {
            finish()
        }

        play.setOnClickListener {
            playbackControl()
        }
    }

    private fun configureMediaPlayer() {
        presenter.prepareTrack(track.previewUrl)  // Загрузка трека при инициализации
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacksAndMessages(null)
        presenter.release()
    }

    private fun getYearFromDate(dateString: String): Int {
        return ZonedDateTime.parse(dateString).year
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                presenter.pause()
                playerState = STATE_PAUSED
                play.setImageResource(R.drawable.ic_button_play)
                Log.d("PlayerActivity", "Paused playback")
            }
            STATE_PREPARED, STATE_PAUSED -> {
                presenter.play()
                playerState = STATE_PLAYING
                play.setImageResource(R.drawable.ic_button_pause)
                Log.d("PlayerActivity", "Started playback")
            }
        }
    }

    private fun updatePlaybackState(isPlayable: Boolean) {
        if (isPlayable) {
            play.isEnabled = false
            playerState = STATE_PREPARED
        } else {
            play.isEnabled = true
            presenter.pause()
            playerState = STATE_PAUSED
        }
    }

    private fun getCoverArtwork(artworkUrl100: String) = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}

/*
 private fun preparePlayer(){
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacksAndMessages(null)
            play.setImageResource(R.drawable.ic_button_play)
            timer.text = "00:00"
        }
    }

    private fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler.post(elapsedTimeTrack())
        play.setImageResource(R.drawable.ic_button_pause)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        play.setImageResource(R.drawable.ic_button_play)
    }
 */
