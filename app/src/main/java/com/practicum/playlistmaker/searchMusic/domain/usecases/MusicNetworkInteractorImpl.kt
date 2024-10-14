package com.practicum.playlistmaker.searchMusic.domain.usecases

import com.practicum.playlistmaker.searchMusic.domain.api.MusicNetworkInteractor
import com.practicum.playlistmaker.searchMusic.domain.repository.MusicNetworkRepository
import java.util.concurrent.Executors

//Этот класс Реализация итерактора MusicNetworkInteractor
class MusicNetworkInteractorImpl(private val repository: MusicNetworkRepository): MusicNetworkInteractor{
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: MusicNetworkInteractor.MusicConsumer) {
        executor.execute {
            consumer.consumer(repository.searchMusic(expression))
        }
    }
}