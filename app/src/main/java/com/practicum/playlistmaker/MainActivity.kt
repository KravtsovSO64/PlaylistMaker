package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.main_button_search)
        val buttonMedia = findViewById<Button>(R.id.main_button_media)
        val buttonSetting = findViewById<Button>(R.id.main_button_setting)


        val buttClickListener : View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }

        buttonSearch.setOnClickListener(buttClickListener)

        buttonMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonSetting.setOnClickListener {
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingIntent)
        }
    }
}