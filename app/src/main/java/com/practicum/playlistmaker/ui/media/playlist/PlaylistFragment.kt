package com.practicum.playlistmaker.ui.media.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState
import com.practicum.playlistmaker.presentation.media.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    companion object {
        fun newInstance(): PlaylistFragment = PlaylistFragment()
    }

    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: PlaylistAdapter? = null

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

        getStateView()
        clickHandler()
        createRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        recyclerView = null
    }

    private fun getStateView() {
        viewModel.getPlaylists()

        viewModel.stateView.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistViewState) {
        when (state) {
            is PlaylistViewState.Empty -> {
                showErrorMessage(state.enableErrorMessage)
            }
            is PlaylistViewState.Content -> {
                showErrorMessage(state.enableErrorMessage)
                adapter = PlaylistAdapter(state.playlist)
                adapter?.notifyDataSetChanged()
                recyclerView?.adapter = adapter
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
}