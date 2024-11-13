package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.search.domain.models.Track

sealed class Result<out T> {
    data class Success<out T>(val data: List<Track>, val code: Int) : Result<T>()
    data class Failure(val code: Int) : Result<Nothing>()
}