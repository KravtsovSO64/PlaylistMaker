package com.practicum.playlistmaker.ui.search.uicomponents

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.model.Track

class TrackAdapter(val listener: OnTrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    var searchListAdapter = ArrayList<Track>()

    fun updateSearchList(list: List<Track>){
        if (!list.isNullOrEmpty()) {
            searchListAdapter = ArrayList<Track>(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchListAdapter[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(searchListAdapter[position])
        }
    }

    override fun getItemCount(): Int = searchListAdapter.size
}

fun interface OnTrackClickListener {
    fun onItemClick(track: Track)
}