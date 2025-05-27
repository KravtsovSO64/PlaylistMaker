package com.practicum.playlistmaker.presentation.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = false)
@Composable
fun SearchScreen() {
    var text by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.backgroundApp)),
                title = {
                    Text(
                        text = stringResource(R.string.search),
                        style = TextStyle(
                            color = colorResource(R.color.basic_text),
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontSize = 22.sp,
                            fontWeight = FontWeight(500)
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = colorResource(R.color.backgroundApp))
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 8.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                label = { Text(stringResource(R.string.search))},
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = colorResource(R.color.gray_black),

                    )
                }
            )
        }
    }
}

