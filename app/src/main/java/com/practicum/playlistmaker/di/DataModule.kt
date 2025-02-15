package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.repositories.local.LocalStorage
import com.practicum.playlistmaker.search.data.repositories.local.SharedPrefsMusicStorage
import com.practicum.playlistmaker.search.data.repositories.network.MusicApiService
import com.practicum.playlistmaker.search.data.repositories.network.NetworkClient
import com.practicum.playlistmaker.search.data.repositories.network.RetrofitNetworkClient
import com.practicum.playlistmaker.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<MusicApiService> {
        Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MusicApiService::class.java)
    }

    single { MediaPlayer() }

    single {
        androidContext()
            .getSharedPreferences(Constants.HISTORY_SEARCH, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<LocalStorage> { SharedPrefsMusicStorage(get(),get()) }
    single<NetworkClient> { RetrofitNetworkClient(get(), androidContext())  }
}