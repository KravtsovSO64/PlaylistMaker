package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.media.viewmodel.viewmodel.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavoriteTracks: Fragment() {

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding  get() = _binding!!

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        fun newInstance(): FragmentFavoriteTracks = FragmentFavoriteTracks()
    }
}
