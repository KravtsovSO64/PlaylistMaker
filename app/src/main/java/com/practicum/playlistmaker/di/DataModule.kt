package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.converter.PlaylistDbConverter
import com.practicum.playlistmaker.data.db.converter.TrackDbConverter
import com.practicum.playlistmaker.data.repositories.search.converter.TrackConverter
import com.practicum.playlistmaker.data.repositories.search.local.LocalStorage
import com.practicum.playlistmaker.data.repositories.search.local.SharedPrefsMusicStorage
import com.practicum.playlistmaker.data.repositories.search.network.MusicApiService
import com.practicum.playlistmaker.data.repositories.search.network.NetworkClient
import com.practicum.playlistmaker.data.repositories.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    factory { Gson() }
    factory { TrackDbConverter() }
    factory { TrackConverter() }
    factory { PlaylistDbConverter() }

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

    single<LocalStorage> { SharedPrefsMusicStorage(get(),get()) }
    single<NetworkClient> { RetrofitNetworkClient(get(), androidContext())  }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "favourite_tracks.db")
        .build()
    }
}