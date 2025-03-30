package com.practicum.playlistmaker.presentation.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist

class PlaylistAdapter(private val playlists: List<Playlist>, val listener: OnPlaylistClickListener): RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_bs_item, parent,false))

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