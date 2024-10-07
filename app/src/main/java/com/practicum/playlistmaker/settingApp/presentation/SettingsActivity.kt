package com.practicum.playlistmaker.settingApp.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.searchMusic.creator.App
import com.practicum.playlistmaker.settingApp.data.repositories.ThemePreferenceRepositoryImpl
import com.practicum.playlistmaker.settingApp.domain.iterator.ThemeSwitcherInteractorImpl



class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var themeSwitcher: ThemeSwitcherInteractorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val themePreferenceManager = ThemePreferenceRepositoryImpl(this)
        themeSwitcher = ThemeSwitcherInteractorImpl(themePreferenceManager)

        setupUI()
    }

    private fun setupUI() {
        binding.arrowBack.setOnClickListener { finish() }
        binding.share.setOnClickListener { shareApp() }
        binding.support.setOnClickListener { sendSupportEmail() }
        binding.arrow.setOnClickListener { openUserAgreement() }

        binding.themeSwitcher.isChecked = themeSwitcher.isDarkThemeEnabled()
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            themeSwitcher.switchTheme(isChecked)
            (applicationContext as App).switchTheme(isChecked)
        }
    }

    private fun sendSupportEmail() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.supportMail)))
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.supportTitleMessage))
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.supportMessage))
        }
        startActivity(supportIntent)
    }

    private fun openUserAgreement() {
        val arrowIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(resources.getString(R.string.userAgreement))
        }
        startActivity(arrowIntent)
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.shareApp))
        }
        startActivity(Intent.createChooser(shareIntent, ""))
    }
}