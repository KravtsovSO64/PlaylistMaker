package com.practicum.playlistmaker.presentation.media.ui.playlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.search.ui.OnTrackClickListener
import com.practicum.playlistmaker.presentation.search.ui.TrackViewHolder

class TrackPLAdapter(
    private val list: List<Track>,
    private val listener: OnTrackClickListener,
    private val longListener: OnTrackLongClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = list[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            listener.onItemClick(track)
        }
        holder.itemView.setOnLongClickListener {
            longListener.onItemLongClick(track)
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnTrackLongClickListener {
        fun onItemLongClick(track: Track): Boolean
    }
}

