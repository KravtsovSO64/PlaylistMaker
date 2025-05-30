package com.practicum.playlistmaker.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.practicum.playlistmaker.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(R.color.backgroundApp),
    titleText: String = "TopAppBar",
    titleColor: Color = colorResource(R.color.basic_text),
    fontFamily: FontFamily = FontFamily(Font(R.font.ys_display_medium)),
    fontWeight: FontWeight = FontWeight(500),

    ) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor),
        title = {
            Text(
                text = titleText,
                color = titleColor,
                fontFamily = fontFamily,
                fontWeight = fontWeight,
            )
        }
    )
}