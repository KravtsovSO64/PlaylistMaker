package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrowBack = binding.arrowBack
        val shareButton = binding.share
        val supportButton =binding.support
        val arrowButton = binding.arrow


        supportButton.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.supportMail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.supportTitleMessage))
            supportIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.supportMessage))
            startActivity(supportIntent)
        }

        arrowButton.setOnClickListener{
            val arrowIntent = Intent(Intent.ACTION_VIEW)
            arrowIntent.data = Uri.parse(resources.getString(R.string.userAgreement))
            startActivity(arrowIntent)
        }

        shareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.shareApp))
            startActivity(Intent.createChooser(shareIntent, ""))
        }

        arrowBack.setOnClickListener {
            finish()
        }
    }
}