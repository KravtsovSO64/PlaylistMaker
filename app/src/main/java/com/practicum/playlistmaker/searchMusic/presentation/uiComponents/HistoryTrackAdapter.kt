package com.practicum.playlistmaker.searchMusic.presentation.uiComponents

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.searchMusic.domain.models.Track

class HistoryTrackAdapter(private val listener : OnTrackClickListener): RecyclerView.Adapter<TrackViewHolder>() {

    lateinit var historyListAdapter: ArrayList<Track>

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
