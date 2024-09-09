package com.practicum.playlistmaker

import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto

class MusicResponce(val resultCount: Int,
                    val results: List<TrackDto>)