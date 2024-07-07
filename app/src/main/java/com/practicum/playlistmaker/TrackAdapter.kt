package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var musicAdapter = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(musicAdapter.get(position))
    }

    override fun getItemCount(): Int = musicAdapter.size
}