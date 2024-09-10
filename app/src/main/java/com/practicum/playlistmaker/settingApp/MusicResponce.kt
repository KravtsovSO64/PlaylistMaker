package com.practicum.playlistmaker.settingApp

import com.practicum.playlistmaker.searchMusic.data.dto.TrackDto

class MusicResponce(val resultCount: Int,
                    val results: List<TrackDto>)