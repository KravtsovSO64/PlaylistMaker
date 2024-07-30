package com.practicum.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import java.time.ZonedDateTime


class PlayerActivity: AppCompatActivity() {

    private lateinit var binding : ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trackNamePlayer.text = intent.getStringExtra("trackName")
        binding.artistNamePlayer.text = intent.getStringExtra("artistName")
        binding.durationTrackPlayer.text = intent.getStringExtra("trackTimeMillis")
        binding.albumTrackPlayer.text = intent.getStringExtra("collectionName")
        binding.releaseYearTrackPlayer.text = intent.getStringExtra("releaseDate")
            ?.let { getYearFromDate(it).toString() }
        binding.styleTrackPlayer.text = intent.getStringExtra("primaryGenreName")
        binding.countryTrackPlayer.text = intent.getStringExtra("country")

        Glide.with(this)
            .load(intent.getStringExtra("artworkUrl100"))
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.placeHolderPlayer)


        binding.arrowBackPlayer.setNavigationOnClickListener {
            finish()
        }

    }

    private fun getYearFromDate(dateString: String): Int {
        return ZonedDateTime.parse(dateString).year
    }

}
