package com.practicum.playlistmaker.presentation.player.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.state.AddTrackStatus
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState
import com.practicum.playlistmaker.presentation.player.service.PlayerService
import com.practicum.playlistmaker.presentation.player.state.PlayerState
import com.practicum.playlistmaker.presentation.player.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.utils.NetworkBroadcastReceiver
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime

class PlayerFragment : Fragment(), PlaylistAdapter.OnPlaylistClickListener {

    companion object {
        private const val ARGS_TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY = 300L

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }

    private val binding get() = _binding!!
    private val viewModel by viewModel<PlayerViewModel>()
    private val networkBroadcastReceiver = NetworkBroadcastReceiver()

    private var isClickAllowed = true
    private var _binding: FragmentPlayerBinding? = null
    private lateinit var track: Track
    private lateinit var timer: TextView
    private lateinit var recyclerView: RecyclerView
    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
           val binder = service as PlayerService.PlayerServiceBinder
            viewModel.setAudioPlayerControl(binder.getPlayerService())
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
           viewModel.removeAudioPlayerControl()
            isServiceBound = false
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), "Can't bind service!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = arguments?.getSerializable(ARGS_TRACK) as? Track
            ?: throw IllegalArgumentException("Track cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startUI()
        clickHandler()
        setupEdgeToEdge()
        bottomSheetManagement()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

        viewModel.setTrack(track)
        viewModel.playerState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
        }

    }

    override fun onStart() {
        viewModel.hideNotification()
        super.onStart()
    }

    override fun onStop() {
        viewModel.showNotification()
        super.onStop()
    }

    private fun bindMusicService() {
        if (!isServiceBound) {

            val intent = Intent(requireContext(), PlayerService::class.java).apply {
                putExtra("TRACK_URL", track.previewUrl)
                putExtra("TRACK_NAME", track.trackName)
                putExtra("ARTIST_NAME", track.artistName)
            }

            requireContext().bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
            isServiceBound = true
        }
    }

    private fun unbindMusicService() {
        if (isServiceBound) {
            requireContext().unbindService(serviceConnection)
            isServiceBound = false
        }
    }

    override fun onPause() {
        showBottomNavigation(true)
        requireContext().unregisterReceiver(networkBroadcastReceiver)
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        unbindMusicService()
        super.onDestroy()
    }

    override fun onResume() {
        showBottomNavigation(false)
        ContextCompat.registerReceiver(
            requireContext(),
            networkBroadcastReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"),
            ContextCompat.RECEIVER_NOT_EXPORTED)
        super.onResume()
    }

    private fun clickHandler() {
        binding.apply {

            buttonPlayStop.onClickPlayBack = {
               viewModel.onPlayerButtonClicked()
            }

            arrowBackPlayer.setNavigationOnClickListener {
                unbindMusicService()
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

        val buttonPlayStop = binding.buttonPlayStop

        when (state) {
            is PlayerState.Default -> {
                viewModel.hideNotification()
                updateCurrentPosition(state.progress)
                buttonPlayStop.isPlaying(state.buttonState)
            }
            is PlayerState.Prepared -> {
                viewModel.hideNotification()
                updateCurrentPosition(state.progress)
                buttonPlayStop.isPlaying(state.buttonState)
            }
            is PlayerState.Playing -> {
                updateCurrentPosition(state.progress)
                buttonPlayStop.isPlaying(state.buttonState)
            }
            is PlayerState.Paused -> {
                viewModel.hideNotification()
                updateCurrentPosition(state.progress)
                buttonPlayStop.isPlaying(state.buttonState)

            }
        }

    }

    private fun startUI() {

        viewModel.isFavoriteTrack.observe(viewLifecycleOwner) { isFavorite ->
            binding.buttonIsFavoritePlayer.setImageResource(
                if (isFavorite) R.drawable.ic_button_is_favourite_track else R.drawable.ic_button_is_not_favourite_track
            )
        }

        binding.apply {
            trackNamePlayer.text = track.trackName
            artistNamePlayer.text = track.artistName
            durationTrackPlayer.text = formatDuration(track.trackTimeMillis)
            albumTrackPlayer.text = track.collectionName
            releaseYearTrackPlayer.text = track.releaseDate?.let { getYearFromDate(it).toString() }
            styleTrackPlayer.text = track.primaryGenreName
            countryTrackPlayer.text = track.country
        }

        Glide.with(this)
            .load(getCoverArtwork(track.artworkUrl100.toString()))
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.placeHolderPlayer)

        timer = binding.trackElapsedTimePlayer

    }

    private fun onFavoriteClicked() {
        if (!track.isFavorite) {
            viewModel.addTrackToFavorite(track)
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
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val divider = requireActivity().findViewById<View>(R.id.divider)

        if (flag) {
            bottomNavigationView.show()
            divider.show()
        } else {
            bottomNavigationView.gone()
            divider.gone()
        }
    }

    private fun bottomSheetManagement() {
        val bottomSheetContainer = binding.bottomSheet
        val overlay = binding.overlay

        recyclerView = binding.includedHeader.playlists
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.apply {

            buttonAddToPlaylist.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            }

            includedHeader.buttonCreatePlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_fragmentCreatePlaylist, null)
            }
        }

        viewModel.playlistState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistViewState.Empty -> {

                }

                is PlaylistViewState.Content -> {
                    recyclerView.adapter = PlaylistAdapter(state.playlist, listener = this)
                }
            }
        }

        viewModel.addTrackStatus.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AddTrackStatus.Success -> {
                    Toast.makeText(requireContext(), " Добавлено в плейлист ${state.namePlaylist}", Toast.LENGTH_SHORT)
                        .show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
                is AddTrackStatus.AlreadyExists -> {
                    Toast.makeText(requireContext(), "Трек уже добавлен в плейлист ${state.namePlaylist}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        viewModel.getPlaylists()
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = (slideOffset + 1) / 2
            }
        })

    }

    override fun onItemClick(playlist: Playlist) {
        if (clickDebounce()) {
            viewModel.addTrackToPlaylist(playlist.id, track)
        }
    }

    private fun clickDebounce(): Boolean {
        val currentState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return currentState
    }

}
