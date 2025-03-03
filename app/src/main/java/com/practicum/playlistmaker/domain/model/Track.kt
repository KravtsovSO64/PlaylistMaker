package com.practicum.playlistmaker.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Track(
    var trackName: String? = "Неизвестный трек",
    var artistName: String? = "Неизвестный артист",
    var trackTimeMillis: Int,
    var artworkUrl100: String? = "",
    var trackId: Int,
    var collectionName: String? = "Неизвестный альбом",
    var releaseDate: String? = "Дата неизвестна",
    var primaryGenreName: String? = "Неизвестный жанр",
    var country: String? = "Неизвестная страна",
    var previewUrl: String? = "",
    var isFavorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeInt(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeInt(trackId)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(previewUrl)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}