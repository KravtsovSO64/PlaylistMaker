package com.practicum.playlistmaker.domain.api.search

import com.practicum.playlistmaker.domain.model.Track

interface MusicLocalRepository {
  fun getHistoryMusic(): List<Track>
  fun setTrackToHistoryMusic(track: Track)
  fun removeHistoryMusic()
}