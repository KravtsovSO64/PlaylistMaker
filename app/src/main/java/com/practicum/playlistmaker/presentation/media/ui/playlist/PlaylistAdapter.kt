package com.practicum.playlistmaker.presentation.media.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist

class PlaylistAdapter(private var playlists: List<Playlist>, private val listener: OnPlaylistClickListener): RecyclerView.Adapter<PlaylistViewHolder>() {

    fun set(newPlaylist: List<Playlist>) {
        playlists = newPlaylist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent,false))

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(playlist = playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface OnPlaylistClickListener {
        fun onItemClick(playlist: Playlist)
    }

}
