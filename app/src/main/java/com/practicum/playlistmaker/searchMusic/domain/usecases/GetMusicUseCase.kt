package com.practicum.playlistmaker.searchMusic.domain.usecases

import com.practicum.playlistmaker.searchMusic.domain.api.MusicInteractor
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicRepository
import java.util.concurrent.Executors


class GetMusicUseCase(private val repository: MusicRepository): MusicInteractor{
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: MusicInteractor.MusicConsumer) {
        executor.execute {
            consumer.consumer(repository.searchMusic(expression))
        }
    }
}