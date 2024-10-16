package com.practicum.playlistmaker.searchMusic.presentation.uiComponents

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

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
        trackTime.text = getTrackTime(track.trackTimeMillis)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkUrl100)
    }

    fun getTrackTime(trackTimeMillis : Int): String {
        val formatTime = SimpleDateFormat("mm:ss", Locale.getDefault())
        return formatTime.format(trackTimeMillis).toString()
    }
}



