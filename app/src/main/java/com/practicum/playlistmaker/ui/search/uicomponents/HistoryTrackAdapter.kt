package com.practicum.playlistmaker.ui.search.uicomponents

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.model.Track

class HistoryTrackAdapter(private val listener : OnTrackClickListener): RecyclerView.Adapter<TrackViewHolder>() {

    var historyListAdapter = ArrayList<Track>()

    fun updateSearchList(list: List<Track>){
        if (!list.isNullOrEmpty()) {
            historyListAdapter = ArrayList<Track>(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(historyListAdapter[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = historyListAdapter.size
}
