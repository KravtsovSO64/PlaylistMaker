package com.practicum.playlistmaker.ui.player

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.player.state.PlayerState
import com.practicum.playlistmaker.presentation.player.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime

class PlayerFragment : Fragment() {

    companion object {
        private const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }

    private val binding get() = _binding!!
    private val viewModel by viewModel<PlayerViewModel>()

    private var _binding: FragmentPlayerBinding? = null
    private lateinit var track: Track
    private lateinit var timer: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var divider: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = arguments?.getSerializable(ARGS_TRACK) as? Track ?: throw IllegalArgumentException("Track cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickHandler()
        setupEdgeToEdge()
        setupUI()

        viewModel.setData(track)
        viewModel.setAudioUrl(track.previewUrl.toString())

        showBottomNavigation(false)

        viewModel.playerState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
        }

        viewModel.setupListeners()
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation(true)
        if (viewModel.playerState.value?.isPlaying == true) {
            viewModel.togglePlayback()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.stop()
    }

    private fun clickHandler() {
        binding.apply {

            buttonPlayStopPlayer.setOnClickListener {
                viewModel.togglePlayback()
            }

            arrowBackPlayer.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            buttonIsFavoritePlayer.setOnClickListener {
                onFavoriteClicked()
            }
        }
    }

    private fun setupEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.setDecorFitsSystemWindows(false)
            ViewCompat.setOnApplyWindowInsetsListener(
                requireActivity().findViewById(android.R.id.content)
            ) { v: View, insets: WindowInsetsCompat ->
                val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                v.setPadding(0, statusBarHeight, 0, 0)
                insets
            }
        }
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

    private fun setupUI() {

        binding.apply {
            trackNamePlayer.text = track.trackName
            artistNamePlayer.text = track.artistName
            durationTrackPlayer.text = formatDuration(track.trackTimeMillis)
            albumTrackPlayer.text = track.collectionName
            releaseYearTrackPlayer.text = track.releaseDate?.let { getYearFromDate(it).toString() }
            styleTrackPlayer.text = track.primaryGenreName
            countryTrackPlayer.text = track.country
            buttonIsFavoritePlayer.setImageResource(
                if (track.isFavorite) R.drawable.ic_button_is_favourite_track else R.drawable.ic_button_is_not_favourite_track
            )
            buttonPlayStopPlayer.setImageResource(R.drawable.ic_button_play)
        }

        Glide.with(this)
            .load(getCoverArtwork(track.artworkUrl100.toString()))
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.placeHolderPlayer)

        timer = binding.trackElapsedTimePlayer

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        divider =requireActivity().findViewById(R.id.divider)
    }

    private fun onFavoriteClicked() {
        if (!track.isFavorite) {
            viewModel.insertFavouriteTrack(track)
        } else {
            viewModel.deleteTrackFromFavourite(track)
        }
    }

    private fun updateCurrentPosition(elapsedTime: String) {
        timer.text = elapsedTime
    }

    private fun getYearFromDate(dateString: String): Int {
        return ZonedDateTime.parse(dateString).year
    }

    private fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")

    private fun formatDuration(millis: Int): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun showBottomNavigation(flag: Boolean) {
        if (flag) {
            bottomNavigationView.show()
            divider.show()
        } else {
            bottomNavigationView.gone()
            divider.gone()
        }
    }
}
