package com.practicum.playlistmaker.ui.media.playlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val coverImage = itemView.findViewById<ImageView>(R.id.playlist_cover_image)
    private val name = itemView.findViewById<TextView>(R.id.playlist_name)
    private val countTracks = itemView.findViewById<TextView>(R.id.playlist_count)

    fun bind(playlist: Playlist) {
        Glide.with(itemView.context)
            .load(playlist.coverImagePath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_place_holder) // Заглушка при загрузке
                    .transform(RoundedCorners(8)) // Закругление углов (8dp)
            )
            .into(coverImage)
        name.text = playlist.name
        countTracks.text = getStringFrom(playlist.trackCount)
    }

    private fun getStringFrom(quantity: Int): String {
        val mod10 = quantity % 10
        val mod100 = quantity % 100

        return when {
            mod10 == 1 && mod100 != 11 -> "$quantity трек"
            mod10 in 2..4 && mod100 !in 12..14 -> "$quantity трека"
            else -> "$quantity треков"
        }
    }

}