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

    companion object {
        private const val NUMBER = "number"

        fun newInstance(number: Int) = FragmentFavoriteTracks().apply {
            arguments = Bundle().apply {
                putInt(NUMBER, number)
            }
        }
    }

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }




}