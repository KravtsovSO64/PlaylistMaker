package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parentView : View) : RecyclerView.ViewHolder(parentView) {
    private val trackName : TextView
    private val artistName :  TextView
    private val trackTime : TextView
    private val artworkUrl100 : ImageView
    private val item : View

    init {
        trackName = parentView.findViewById(R.id.track_name)
        artistName = parentView.findViewById(R.id.artist_name)
        trackTime = parentView.findViewById(R.id.track_time)
        artworkUrl100 = parentView.findViewById(R.id.album_cover)
        item = parentView
    }
    fun bind (model : Track){
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(item)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkUrl100)
    }
}