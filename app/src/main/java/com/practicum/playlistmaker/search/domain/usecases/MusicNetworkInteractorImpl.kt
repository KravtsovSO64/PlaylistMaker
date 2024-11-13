package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.MusicNetworkInteractor
import com.practicum.playlistmaker.search.domain.repository.MusicNetworkRepository

import java.util.concurrent.Executors

//Этот класс Реализация итерактора MusicNetworkInteractor
class MusicNetworkInteractorImpl(private val repository: MusicNetworkRepository) : MusicNetworkInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: MusicNetworkInteractor.MusicConsumer) {
        executor.execute {
            repository.searchMusic(expression) { result ->
                consumer.consumer(result)
            }
        }
    }
}
