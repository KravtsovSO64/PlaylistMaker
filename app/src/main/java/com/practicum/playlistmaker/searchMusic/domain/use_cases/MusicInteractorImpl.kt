package com.practicum.playlistmaker.searchMusic.domain.use_cases

import com.practicum.playlistmaker.searchMusic.domain.api.MusicInteractor
import com.practicum.playlistmaker.searchMusic.domain.api.MusicRepository
import java.util.concurrent.Executors


class MusicInteractorImpl(private val repository: MusicRepository): MusicInteractor{
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: MusicInteractor.MusicConsumer) {
        executor.execute {
            consumer.consumer(repository.searchMusic(expression))
        }
    }

}