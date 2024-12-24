package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.viewmodel.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlayList: Fragment() {

    companion object {
        private const val NUMBER = "number"

        fun newInstance(number: Int) = FragmentPlayList().apply {
            arguments = Bundle().apply {
                putInt(NUMBER, number)
            }
        }
    }

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }
}