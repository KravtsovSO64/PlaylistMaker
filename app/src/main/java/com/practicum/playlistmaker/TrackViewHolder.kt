package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parentView : ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parentView.context)
            .inflate(R.layout.track_view, parentView, false)) {
    private var trackName: TextView = itemView.findViewById(R.id.track_name)
    private var artistName: TextView = itemView.findViewById(R.id.artist_name)
    private var trackTime: TextView = itemView.findViewById(R.id.track_time)
    private var artworkUrl100: ImageView = itemView.findViewById(R.id.album_cover)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.getTrackTime()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkUrl100)
    }
}



