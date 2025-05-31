package com.practicum.playlistmaker.presentation.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.media.ui.playlist.PlaylistFragment


class MediaFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MediaScreen(
                    onCreatePlaylistClick = {
                        navController.navigate(R.id.action_mediaFragment_to_fragmentCreatePlaylist)
                    },
                    onPlaylistClick = { playlist ->
                        navController.navigate(
                            R.id.action_mediaFragment_to_playlistFragment,
                            PlaylistFragment.createArgs(playlist)
                        )
                    },
                    navController
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }
}
