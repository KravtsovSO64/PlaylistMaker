package com.practicum.playlistmaker.ui.media.uicomponents

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.ui.search.uicomponents.OnTrackClickListener

class FavouriteTracksAdapter(private val listener : OnTrackClickListener): RecyclerView.Adapter<FavouriteTracksViewHolder>() {

    private var favouriteListAdapter = ArrayList<Track>()

    fun getList(): List<Track> {
        return favouriteListAdapter.toList()
    }

    fun setList(list: List<Track>) {
        favouriteListAdapter.clear()
        favouriteListAdapter.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteTracksViewHolder = FavouriteTracksViewHolder(parent)

    override fun getItemCount(): Int = favouriteListAdapter.size

    override fun onBindViewHolder(holder: FavouriteTracksViewHolder, position: Int) {
        holder.bind(favouriteListAdapter[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }
}