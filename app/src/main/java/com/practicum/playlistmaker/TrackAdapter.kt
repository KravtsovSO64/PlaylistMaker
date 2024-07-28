package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val listener : OnTrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    var searchListAdapter = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchListAdapter[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = searchListAdapter.size
}

fun interface OnTrackClickListener {
    fun onItemClick(position: Int)
}