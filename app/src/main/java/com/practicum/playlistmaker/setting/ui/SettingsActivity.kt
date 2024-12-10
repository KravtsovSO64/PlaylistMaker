package com.practicum.playlistmaker.setting.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.setting.presentation.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val themeViewModel: ThemeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arrowBack.setOnClickListener { finish() }
        binding.share.setOnClickListener { shareApp() }
        binding.support.setOnClickListener { sendSupportEmail() }
        binding.arrow.setOnClickListener { openUserAgreement() }
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked -> themeViewModel.switchTheme(isChecked) }

        themeViewModel.isDarkThemeEnabled.observe(this) {
            binding.themeSwitcher.isChecked = it
            applyTheme(it)
        }

        applyTheme(themeViewModel.isDarkThemeEnabled.value == true)
    }

    private fun applyTheme(isDarkTheme: Boolean) {
        setDefaultNightMode(if (isDarkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO)
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
