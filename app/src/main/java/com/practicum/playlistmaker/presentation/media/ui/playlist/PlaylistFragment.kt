package com.practicum.playlistmaker.presentation.media.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.presentation.player.ui.PlayerFragment
import com.practicum.playlistmaker.presentation.search.ui.OnTrackClickListener
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.formatDuration
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment(), OnTrackClickListener, TrackPLAdapter.OnTrackLongClickListener {

    companion object {
        const val ARGS_PLAYLIST = "playlist"
        private const val CLICK_DEBOUNCE_DELAY = 300L

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ARGS_PLAYLIST to playlist)
    }

    private var _binding: FragmentPlaylistBinding? = null
    private lateinit var playlist: Playlist
    private var recyclerView: RecyclerView? = null
    private var adapter: TrackPLAdapter? = null
    private var isClickAllowed = true
    private var track: Track? = null
    private var removeTrackDialog: MaterialAlertDialogBuilder? = null
    private var removePlaylistDialog: MaterialAlertDialogBuilder? = null

    private val binding: FragmentPlaylistBinding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = arguments?.getSerializable(ARGS_PLAYLIST) as? Playlist
            ?: throw IllegalArgumentException("Track cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRecyclerView()
        getStateView()
        clickHandler()
        createDialogConfirmation()
        bottomSheetManagement()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation(false)
        isClickAllowed = true
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation(true)
    }

    private fun getStateView() {

        viewModel.getPlaylistById(playlist.id)
        viewModel.getAllTracks(playlist.trackIdsJson)

        with(binding) {
            includedHeader.apply {
                buttonCreatePlaylist.gone()
                addTitle.gone()
            }
        }

        viewModel.track.observe(viewLifecycleOwner) { tracks ->
           val durationSum = tracks.sumOf { it.trackTimeMillis }
            binding.time.text = getMinutesString(durationSum)
            adapter = TrackPLAdapter(tracks, this, this)
            adapter?.notifyDataSetChanged()
            recyclerView?.adapter = adapter
        }

        viewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            this.playlist = playlist
            binding.name.text = playlist.name
            binding.description.text = playlist.description
            binding.tracks.text = getStringFrom(playlist.trackCount)

            Glide.with(this@PlaylistFragment)
                .load(playlist.coverImagePath)
                .placeholder(R.drawable.ic_place_holder)
                .centerCrop()
                .transform(RoundedCorners(2))
                .into(binding.poster)
        }
    }

    private fun clickHandler() {

        with(binding) {
            buttonShare.setOnClickListener {
                sharePlaylist()
            }

            buttonMore.setOnClickListener {

            }

            arrowBackPlayer.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    private fun createRecyclerView() {
        recyclerView = binding.includedHeader.playlists
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getStringFrom(quantity: Int): String {
        val mod10 = quantity % 10
        val mod100 = quantity % 100

        return when {
            mod10 == 1 && mod100 != 11 -> "$quantity трек"
            mod10 in 2..4 && mod100 !in 12..14 -> "$quantity трека"
            else -> "$quantity треков"
        }
    }

    private fun getMinutesString(mililis: Int): String {
        val minutes = (mililis / 1000) / 60
        val mod10 = minutes % 10
        val mod100 = minutes % 100

        return when {
            mod10 == 1 && mod100 != 11 -> "$minutes минута"
            mod10 in 2..4 && mod100 !in 12..14 -> "$minutes минуты"
            else -> "$minutes минут"
        }
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

    override fun onItemClick(track: Track) {
        if (clickDebounce()) openPlayer(track)
    }

    private fun openPlayer(track: Track) {
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment, PlayerFragment.createArgs(track))
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

    private fun createDialogConfirmation() {
        removeTrackDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить трек?")
            .setNegativeButton("Нет") { dialog, which ->

            }.setPositiveButton("Да") { dialog, which ->
                Toast.makeText(requireContext(),"Трек удалён", Toast.LENGTH_SHORT).show()
                viewModel.removeTrackFromPlaylist(playlist, track!!.trackId)
            }

        removePlaylistDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист ${playlist.name}?")
            .setNegativeButton("Нет") { dialog, which ->

            }.setPositiveButton("Да") { dialog, which ->
                Toast.makeText(requireContext(),"Плейлист удалён", Toast.LENGTH_SHORT).show()
                viewModel.deletePlaylist(playlist)
                findNavController().popBackStack()
            }
    }

    private fun sharePlaylist() {
        if (playlist.trackCount > 0) {
            val shareMessage = buildString {
                append("${playlist.name}\n")
                append("${playlist.description}\n")
                append("${playlist.trackCount} треков\n\n")

                viewModel.track.value?.forEachIndexed { index, track ->
                    append(
                        "${index + 1}.${track.artistName} - ${track.trackName} (${
                            formatDuration(
                                track.trackTimeMillis.toLong()
                            )
                        })"
                    )
                }
            }

            activity?.let { context ->
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }
                startActivity(Intent.createChooser(shareIntent, null))
            }
        }
    }

    private fun bottomSheetManagement() {
        val bottomSheetContainer = binding.bottomSheetMenu
        val overlay = binding.overlay
        val includeMenu = binding.includedMenu

        Glide.with(requireContext())
            .load(playlist.coverImagePath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_place_holder)
                    .transform(RoundedCorners(2))
            )
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(includeMenu.playlistCoverImage)

        with(includeMenu) {
            playlistName.text = playlist.name
            playlistCount.text = getStringFrom(playlist.trackCount)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.buttonMore.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        includeMenu.sharePlaylist.setOnClickListener {
            sharePlaylist()
        }

        includeMenu.editPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_editPlaylistFragment, EditPlaylistFragment.createArgs(playlist))
        }

        includeMenu.removePlaylist.setOnClickListener {
            removePlaylistDialog?.show()
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = (slideOffset + 1) / 2
            }
        })

    }

    override fun onItemLongClick(track: Track): Boolean {
        this.track = track
        removeTrackDialog?.show()
        return true
    }
}