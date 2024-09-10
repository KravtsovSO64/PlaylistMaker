package com.practicum.playlistmaker.settingApp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Locale


class PlayerActivity: AppCompatActivity() {

    private lateinit var binding : ActivityPlayerBinding
    private lateinit var play: ImageView
    private lateinit var previewUrl: String
    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var mainThreadHandler: Handler
    private lateinit var timer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())
        timer = binding.trackElapsedTimePlayer

        binding.trackNamePlayer.text = intent.getStringExtra("trackName")
        binding.artistNamePlayer.text = intent.getStringExtra("artistName")
        binding.durationTrackPlayer.text = intent.getStringExtra("trackTimeMillis")
        binding.albumTrackPlayer.text = intent.getStringExtra("collectionName")
        binding.releaseYearTrackPlayer.text = intent.getStringExtra("releaseDate")
            ?.let { getYearFromDate(it).toString() }
        binding.styleTrackPlayer.text = intent.getStringExtra("primaryGenreName")
        binding.countryTrackPlayer.text = intent.getStringExtra("country")
        previewUrl = intent.getStringExtra("previewUrl").toString()
        play = binding.buttonPlayStopPlayer
        preparePlayer()

        play.setImageResource(R.drawable.ic_button_play)

        Glide.with(this)
            .load(intent.getStringExtra("artworkUrl100"))
            .placeholder(R.drawable.ic_place_holder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.placeHolderPlayer)


        binding.arrowBackPlayer.setNavigationOnClickListener {
            finish()
        }

        play.setOnClickListener {
            playbackControl()
        }

    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }
    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }

    private fun getYearFromDate(dateString: String): Int {
        return ZonedDateTime.parse(dateString).year
    }

    private fun preparePlayer(){
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacksAndMessages(null)
            play.setImageResource(R.drawable.ic_button_play)
            timer.text = "00:00"
        }
    }

    private fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler.post(elapsedTimeTrack())
        play.setImageResource(R.drawable.ic_button_pause)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        play.setImageResource(R.drawable.ic_button_play)
    }

    private fun playbackControl() {
        when(playerState){
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun elapsedTimeTrack(): Runnable{
        return object : Runnable {
            override fun run() {
                var elapsedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                timer.text = elapsedTime
                mainThreadHandler.postDelayed(this, 300L)
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}
