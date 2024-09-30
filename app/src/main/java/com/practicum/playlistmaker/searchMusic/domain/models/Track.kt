package com.practicum.playlistmaker.searchMusic.domain.models

import android.os.Parcel
import android.os.Parcelable

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String,
    val trackId: Int,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String,
    val previewUrl: String
) : Parcelable {

    // describeContents обычно возвращает 0
    override fun describeContents(): Int {
        return 0
    }

    // Метод для записи данных объекта в Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeInt(trackTimeMillis)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(artworkUrl100)
        parcel.writeString(previewUrl)
    }

    // Создание объекта из Parcel
    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(
                trackName = parcel.readString() ?: "",
                artistName = parcel.readString() ?: "",
                trackTimeMillis = parcel.readInt(),
                collectionName = parcel.readString() ?: "",
                releaseDate = parcel.readString() ?: "",
                primaryGenreName = parcel.readString() ?: "",
                country = parcel.readString() ?: "",
                artworkUrl100 = parcel.readString() ?: "",
                previewUrl = parcel.readString() ?: "",
                trackId = parcel.readInt()
            )
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}


/*


fun getTrackTime(): String {
    val formatTime = SimpleDateFormat("mm:ss", Locale.getDefault())
    return formatTime.format(trackTimeMillis).toString()
}

 */