package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Constants
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import com.practicum.playlistmaker.player.presentation.view.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var mainThreadHandler: Handler
    private lateinit var timer: TextView
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getTrack()
        viewModel.setAudioUrl(track.previewUrl)
        mainThreadHandler = Handler(Looper.getMainLooper())

        viewModel.playerState.observe(this) { state ->
            updateUI(state)
        }

        viewModel.setupListeners()

        binding.buttonPlayStopPlayer.setOnClickListener {
            viewModel.togglePlayback()
        }

        binding.arrowBackPlayer.setNavigationOnClickListener {
            mainThreadHandler.removeCallbacksAndMessages(null)
            finish()
        }

        setupUI()
    }

    private fun updateUI(state: PlayerState) {
        binding.buttonPlayStopPlayer.setImageResource(
            if (state.isPlaying) R.drawable.ic_button_pause else R.drawable.ic_button_play
        )
        updateCurrentPosition(state.currentPosition)
    }

    private fun getTrack(): Track {
        return intent.getSerializableExtra(Constants.TRACK) as? Track
            ?: throw IllegalArgumentException("Track data required")
    }

    private fun setupUI() {
        binding.trackNamePlayer.text = track.trackName
        binding.artistNamePlayer.text = track.artistName
        binding.durationTrackPlayer.text = formatDuration(track.trackTimeMillis)
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
        if (viewModel.playerState.value?.isPlaying == true) { viewModel.togglePlayback() }
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    private fun updateCurrentPosition(elapsedTime: String) {
        timer.text = elapsedTime
    }

    private fun getYearFromDate(dateString: String): Int {
        return ZonedDateTime.parse(dateString).year
    }

    private fun getCoverArtwork(artworkUrl100: String) = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")

    private fun formatDuration(millis: Int): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

