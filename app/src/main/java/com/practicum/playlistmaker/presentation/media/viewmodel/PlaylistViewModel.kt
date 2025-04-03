package com.practicum.playlistmaker.presentation.media.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.media.playlist.PlaylistIterator
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val iterator: PlaylistIterator
) : ViewModel() {

    private var _stateView = MutableLiveData<PlaylistViewState>()
    val stateView: LiveData<PlaylistViewState> get() = _stateView

    private var _tracks = MutableLiveData<List<Track>>()
    val track: LiveData<List<Track>> get() = _tracks


    fun createPlaylist(name: String, description: String, coverImagePath: String) {
        val playlist =
            Playlist(name = name, description = description, coverImagePath = coverImagePath)
        viewModelScope.launch {
            iterator.insert(playlist)
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            iterator
                .getPlaylists()
                .collect { playlist ->
                    processResult(playlist)
                }
        }
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch {
            iterator.saveImageToPrivateStorage(uri)
        }
    }

    fun getAllTracks(trackIdsJson: String) {
        viewModelScope.launch {
            iterator
                .getAllTracks(trackIdsJson)
                .collect { track ->
                    _tracks.postValue(track)
                }
        }
    }

    fun removeTrackFromPlaylist(playlist: Playlist, trackId: Int) {
        viewModelScope.launch {
            iterator.removeTrackFromPlaylist(playlist, trackId)
        }
    }

    private fun processResult(playlist: List<Playlist>) {
        if (playlist.isEmpty()) {
            _stateView.postValue(PlaylistViewState.Empty(true))
        } else {
            _stateView.postValue(PlaylistViewState.Content(playlist, false))
        }
    }
}