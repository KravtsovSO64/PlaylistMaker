package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pagerViewMedia.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.arrowBack.setOnClickListener { finish() }

        tabMediator = TabLayoutMediator(binding.tabViewMedia, binding.pagerViewMedia) {tab, position  ->
            when(position) {
                0 -> tab.setText(R.string.favoriteTrackTab)
                else -> tab.setText(R.string.PlaylistTab)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}