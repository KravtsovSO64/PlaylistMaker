package com.practicum.playlistmaker.presentation.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.model.Track

class TrackAdapter(val listener: OnTrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    private var trackList = ArrayList<Track>()

    fun setList(list: List<Track>){
        if (list.isNotEmpty()) {
            trackList = ArrayList(list)
        }
    }

    fun getList(): List<Track>{
        return trackList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(trackList[position])
        }
    }

    override fun getItemCount(): Int = trackList.size
}

fun interface OnTrackClickListener {
    fun onItemClick(track: Track)
}