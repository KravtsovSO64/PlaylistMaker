package com.practicum.playlistmaker.ui.player

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.player.state.PlayerState
import com.practicum.playlistmaker.presentation.player.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime

class PlayerActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityPlayerBinding

    //Instances class
    private lateinit var track: Track
    private lateinit var timer: TextView

    //ViewModel
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content)
            ) { v: View, insets: WindowInsetsCompat ->
                val statusBarHeight =
                    insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                v.setPadding(0, statusBarHeight, 0, 0)
                insets
            }
        }

        track = getTrack()
        viewModel.setData(track)

        viewModel.setAudioUrl(track.previewUrl.toString())

        viewModel.playerState.observe(this) { state ->
            updateUI(state)
        }

        viewModel.setupListeners()

        binding.buttonPlayStopPlayer.setOnClickListener {
            viewModel.togglePlayback()
        }

        binding.arrowBackPlayer.setNavigationOnClickListener {
            finish()
        }

        binding.buttonIsFavoritePlayer.setOnClickListener { onFavoriteClicked() }

        setupUI()
        ///TAG
        Log.d("toa", "${viewModel.playerState.value?.isFavourite}")
    }

    private fun updateUI(state: PlayerState) {
        binding.buttonPlayStopPlayer.setImageResource(
            if (state.isPlaying) R.drawable.ic_button_pause else R.drawable.ic_button_play
        )
        updateCurrentPosition(state.currentPosition)
        binding.buttonIsFavoritePlayer.setImageResource(
            if (state.isFavourite) R.drawable.ic_button_is_favourite_track else R.drawable.ic_button_is_not_favourite_track
        )
    }

    private fun getTrack(): Track {
        return intent.getSerializableExtra(Constants.TRACK) as? Track
            ?: throw IllegalArgumentException("Track data required")
    }

    private fun onFavoriteClicked() {
        viewModel.run {
            if (!track.isFavorite) insertFavouriteTrack(track) else deleteTrackFromFavourite(track)
        }
    }

    private fun setupUI() {
        binding.trackNamePlayer.text = track.trackName
        binding.artistNamePlayer.text = track.artistName
        binding.durationTrackPlayer.text = formatDuration(track.trackTimeMillis)
        binding.albumTrackPlayer.text = track.collectionName
        binding.releaseYearTrackPlayer.text = track.releaseDate?.let { getYearFromDate(it).toString() }
        binding.styleTrackPlayer.text = track.primaryGenreName
        binding.countryTrackPlayer.text = track.country
        binding.buttonIsFavoritePlayer.setImageResource( if (track.isFavorite) R.drawable.ic_button_is_favourite_track else R.drawable.ic_button_is_not_favourite_track)

        Glide.with(this)
            .load(getCoverArtwork(track.artworkUrl100.toString()))
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
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
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

