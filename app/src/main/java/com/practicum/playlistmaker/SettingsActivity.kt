package com.practicum.playlistmaker

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBack = findViewById<ImageView>(R.id.arrow_back)
        val shareButton = findViewById<FrameLayout>(R.id.share)
        val supportButton =findViewById<FrameLayout>(R.id.support)
        val arrowButton = findViewById<FrameLayout>(R.id.arrow)


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
            val shareButton = Intent(Intent.ACTION_SEND)
            shareButton.setType("text/plain")
            shareButton.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.shareApp))
            startActivity(Intent.createChooser(shareButton, ""))
        }

        arrowBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}