package com.practicum.playlistmaker.presentation.media.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.state.FavouriteTrackViewState
import com.practicum.playlistmaker.presentation.media.viewmodel.FavoriteTracksViewModel
import com.practicum.playlistmaker.presentation.player.ui.PlayerFragment
import com.practicum.playlistmaker.presentation.search.ui.OnTrackClickListener
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment(), OnTrackClickListener {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun newInstance(): FavoriteTracksFragment = FavoriteTracksFragment()
    }

    //Binding
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding  get() = _binding!!

    //Adapter
    private val adapterFavouriteTrack = FavouriteTracksAdapter(listener = this)

    //ViewModel
    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    //ClickDebounce
    private var isClickAllowed = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Recycler and Adapter
        val recyclerFavouriteTrack = binding.favouriteTrackRecycler
        recyclerFavouriteTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerFavouriteTrack.adapter = adapterFavouriteTrack

        favoriteTracksViewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavouriteTrackViewState) {

        when (state) {
            is FavouriteTrackViewState.Empty -> { switchView(false)}
            is FavouriteTrackViewState.Content -> {
                switchView(true)
                adapterFavouriteTrack.setList(state.favoriteList)
                adapterFavouriteTrack.notifyDataSetChanged()
            }
        }

    }

    override fun onResume() {
        favoriteTracksViewModel.getListFavourite()
        isClickAllowed = true
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(track: Track) {
        if (clickDebounce()) {
            transferTrackToPlayer(track)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun transferTrackToPlayer(track: Track) {
        findNavController().navigate(R.id.action_mediaFragment_to_playerFragment, PlayerFragment.createArgs(track))
    }

    private fun switchView(flag: Boolean) {
      if (flag) {
          binding.isEmptyListFavourite.gone()
          binding.favouriteTrackRecycler.show()
      } else {
          binding.isEmptyListFavourite.show()
          binding.favouriteTrackRecycler.gone()
      }
    }

}
