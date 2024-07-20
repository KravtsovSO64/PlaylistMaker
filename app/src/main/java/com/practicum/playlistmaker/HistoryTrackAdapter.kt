package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryTrackAdapter: RecyclerView.Adapter<TrackViewHolder>() {

    var historyListAdapter = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(historyListAdapter.get(position))
    }

    override fun getItemCount(): Int = historyListAdapter.size
}
