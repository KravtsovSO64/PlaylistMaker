package com.practicum.playlistmaker.presentation.setting.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.setting.viewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val themeViewModel: ThemeViewModel = koinViewModel()
    val isDarkTheme by themeViewModel.isDarkThemeEnabled.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green,
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.settings),
                            style = TextStyle(
                                color = colorResource(R.color.basic_text),
                                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                                fontSize = 22.sp,
                                fontWeight = FontWeight(500)
                            )
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = colorResource(R.color.backgroundApp))
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 24.dp)
        ) {
            SettingItem(
                title = stringResource(R.string.dark_theme),
                trailingContent = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { themeViewModel.switchTheme(it) },
                        colors = SwitchDefaults.colors(
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent,
                            checkedThumbColor = colorResource(R.color.blue),
                            checkedTrackColor = colorResource(R.color.light_blue),
                            uncheckedThumbColor = colorResource(R.color.gray),
                            uncheckedTrackColor = colorResource(R.color.light_gray),
                        )
                    )
                }
            )

            SettingItem(
                title = stringResource(R.string.share_app),
                icon = R.drawable.ic_share,
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.shareApp))
                    }
                    context.startActivity(Intent.createChooser(shareIntent, null))
                }
            )

            SettingItem(
                title = stringResource(R.string.write_support),
                icon = R.drawable.ic_support,
                onClick = {
                    val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.supportMail)))
                        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.supportTitleMessage))
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.supportMessage))
                    }
                    context.startActivity(supportIntent)
                }
            )

            SettingItem(
                title = stringResource(R.string.user_agreement),
                icon = R.drawable.ic_arrow,
                onClick = {
                    val arrowIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(context.getString(R.string.userAgreement))
                    }
                    context.startActivity(arrowIntent)
                }
            )
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    icon: Int? = null,
    background: Color = colorResource(R.color.backgroundApp),
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(background)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = colorResource(R.color.basic_text),
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontSize = 16.sp,
                fontWeight = FontWeight(400)
            )
        )

        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = colorResource(R.color.gray_white)
            )
        }

        if (trailingContent != null) {
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                trailingContent()
            }
        }
    }
}
