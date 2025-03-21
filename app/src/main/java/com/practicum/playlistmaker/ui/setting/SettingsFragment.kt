package com.practicum.playlistmaker.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.presentation.setting.viewmodel.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val themeViewModel: ThemeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.share.setOnClickListener { shareApp() }
        binding.support.setOnClickListener { sendSupportEmail() }
        binding.arrow.setOnClickListener { openUserAgreement() }
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked -> themeViewModel.switchTheme(isChecked) }

        themeViewModel.isDarkThemeEnabled.observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
            applyTheme(it)
        }

        applyTheme(themeViewModel.isDarkThemeEnabled.value == true)
    }

    private fun applyTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
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
        startActivity(Intent.createChooser(shareIntent, null))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
