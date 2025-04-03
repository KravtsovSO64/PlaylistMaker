package com.practicum.playlistmaker.presentation.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.presentation.media.ui.favorite.FavoriteTracksFragment
import com.practicum.playlistmaker.presentation.media.ui.playlist.PlaylistsFragment

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
       return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position) {
           0 -> FavoriteTracksFragment.newInstance()
           else -> PlaylistsFragment.newInstance()
       }
    }
}