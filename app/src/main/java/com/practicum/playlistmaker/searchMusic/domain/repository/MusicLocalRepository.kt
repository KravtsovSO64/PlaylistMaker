package com.practicum.playlistmaker.searchMusic.domain.repository

import com.practicum.playlistmaker.searchMusic.domain.models.Track

interface MusicLocalRepository {
  fun getHistoryMusic(): List<Track>
  fun setTrackToHistoryMusic(track: Track)
  fun removeHistoryMusic()
}