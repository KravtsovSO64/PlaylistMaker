package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface MusicLocalRepository {
  fun getHistoryMusic(): List<Track>
  fun setTrackToHistoryMusic(track: Track)
  fun removeHistoryMusic()
}