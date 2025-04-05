package com.practicum.playlistmaker.presentation.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState
import com.practicum.playlistmaker.presentation.media.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment(), PlaylistAdapter.OnPlaylistClickListener {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L

        fun newInstance(): PlaylistsFragment = PlaylistsFragment()
    }

    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

    private var isClickAllowed = true
    private var _binding: FragmentPlaylistsBinding? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: PlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStateView()
        clickHandler()
        createRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        recyclerView = null
    }

    private fun getStateView() {

        viewModel.stateView.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistViewState) {
        when (state) {
            is PlaylistViewState.Empty -> {
                showErrorMessage(state.enableErrorMessage)
                adapter?.set(emptyList())
                adapter?.notifyDataSetChanged()
            }
            is PlaylistViewState.Content -> {
                showErrorMessage(state.enableErrorMessage)
                adapter?.set(state.playlist)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun clickHandler() {
        binding.apply {
            buttonCreatePlaylist.setOnClickListener {
                openViewCreatingPlaylist()
            }
        }
    }

    private fun createRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView!!.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PlaylistAdapter(emptyList(), this)
        recyclerView?.adapter = adapter
    }

    private fun showErrorMessage(enable: Boolean) {
        binding.apply {
            when (enable){
                true -> {
                    nothingFoundImage.show()
                    nothingFoundMessage.show()
                }
                false -> {
                    nothingFoundImage.gone()
                    nothingFoundMessage.gone()
                }
            }
        }
    }

    private fun openViewCreatingPlaylist() {
        findNavController().navigate(R.id.action_mediaFragment_to_fragmentCreatePlaylist, null)
    }

    override fun onItemClick(playlist: Playlist) {
       if (clickDebounce()) {
           findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment, PlaylistFragment.createArgs(playlist))
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