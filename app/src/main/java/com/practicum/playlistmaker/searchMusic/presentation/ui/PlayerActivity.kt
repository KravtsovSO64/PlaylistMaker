package com.practicum.playlistmaker.searchMusic.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.searchMusic.creator.Creator
import com.practicum.playlistmaker.searchMusic.domain.api.MediaPlayerIterator
import com.practicum.playlistmaker.searchMusic.domain.api.PlayerStatus
import com.practicum.playlistmaker.searchMusic.domain.api.PlayerStatusListener
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Locale

class PlayerActivity : AppCompatActivity(), PlayerStatusListener {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var mediaPlayerIterator: MediaPlayerIterator
    private lateinit var mainThreadHandler: Handler
    private lateinit var timer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getParcelableExtra("track")
            ?: throw IllegalArgumentException("Track data required")

        val audioUrl = track.previewUrl

        mediaPlayerIterator = Creator.provideMediaPlayerInteractor()
        mediaPlayerIterator.setupPlayerStatusListener(this)
        mainThreadHandler = Handler(Looper.getMainLooper())

        binding.buttonPlayStopPlayer.setOnClickListener {
            if (mediaPlayerIterator.isPlaying()) {
                mediaPlayerIterator.pause()
                binding.buttonPlayStopPlayer.setImageResource(R.drawable.ic_button_play)
                mainThreadHandler.removeCallbacksAndMessages(null) // Останавливаем обновление времени
            } else {
                if (mediaPlayerIterator.isPlaying()) {
                    mediaPlayerIterator.resume()
                } else {
                    mediaPlayerIterator.play(audioUrl)
                    mainThreadHandler.post(elapsedTimeTrack()) // Начинаем обновление времени сразу после начала проигрывания
                }
                binding.buttonPlayStopPlayer.setImageResource(R.drawable.ic_button_pause)  // Измените иконку на "Пауза"
            }
        }

        binding.arrowBackPlayer.setNavigationOnClickListener {
            mediaPlayerIterator.stop()
            mainThreadHandler.removeCallbacksAndMessages(null)
            finish()
        }

        setupUI()
    }

    private fun setupUI() {
        binding.trackNamePlayer.text = track.trackName
        binding.artistNamePlayer.text = track.artistName
        binding.durationTrackPlayer.text = formatDuration(track.trackTimeMillis).toString()
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

        timer = binding.trackElapsedTimePlayer

        binding.buttonPlayStopPlayer.setImageResource(R.drawable.ic_button_play)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerIterator.pause()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerIterator.stop()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    private fun elapsedTimeTrack(): Runnable {
        return object : Runnable {
            override fun run() {
                    val elapsedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayerIterator.currentPosition())
                    timer.text = elapsedTime
                    mainThreadHandler.postDelayed(this, 300L)
            }
        }
    }

    private fun getYearFromDate(dateString: String): Int {
        return ZonedDateTime.parse(dateString).year
    }

    private fun getCoverArtwork(artworkUrl100: String) = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")

    override fun onPlayerStatusChanged(status: PlayerStatus) {
        when (status) {
            PlayerStatus.PLAYING -> {
                mediaPlayerIterator.isPlaying()
                binding.buttonPlayStopPlayer.setImageResource(R.drawable.ic_button_pause)
            }
            PlayerStatus.PAUSED -> {
                mediaPlayerIterator.pause()
                binding.buttonPlayStopPlayer.setImageResource(R.drawable.ic_button_play)
            }
            PlayerStatus.STOPPED -> mediaPlayerIterator.stop()
            PlayerStatus.COMPLETED -> {
                binding.buttonPlayStopPlayer.setImageResource(R.drawable.ic_button_play)
                mainThreadHandler.removeCallbacksAndMessages(null) // Останавливаем обновление времени
                timer.text = "00:00"
            }
            PlayerStatus.PREPARING -> mediaPlayerIterator.resume()
            PlayerStatus.ERROR ->TODO()
        }
    }

    private fun formatDuration(millis: Int): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
