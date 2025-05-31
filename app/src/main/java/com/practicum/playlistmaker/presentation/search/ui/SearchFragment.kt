package com.practicum.playlistmaker.presentation.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.player.ui.PlayerFragment
import com.practicum.playlistmaker.presentation.search.viewmodel.TrackSearchViewModel
import com.practicum.playlistmaker.utils.NetworkBroadcastReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<TrackSearchViewModel>()
    private val networkBroadcastReceiver = NetworkBroadcastReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(onTrackClick = { track -> openPlayer(track) })
            }
        }
    }

    private fun openPlayer(track: Track) {
        viewModel.setToListHistorySearchMusic(track)
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }
}


