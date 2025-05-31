package com.practicum.playlistmaker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R

@Composable
fun ListItem(
    trackName: String,
    artistName: String,
    trackTime: String,
    coverUrl: String = "",
    onItemClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(colorResource(R.color.backgroundApp))
            .clickable(onClick = onItemClick)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = coverUrl,
                contentDescription = "Album cover",
                placeholder = painterResource(R.drawable.ic_place_holder),
                error = painterResource(R.drawable.ic_place_holder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 40.dp)
            ) {
                Text(
                    text = trackName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    color = colorResource(R.color.search_holder_trackName),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = artistName,
                        color = colorResource(R.color.search_holder_artistName),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        painter = painterResource(R.drawable.ic_ellipse),
                        contentDescription = "Separator",
                        tint = colorResource(R.color.setting_icon_color),
                        modifier = Modifier.size(4.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = trackTime,
                        color = colorResource(R.color.search_holder_artistName),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        maxLines = 1
                    )
                }
            }


            Icon(
                painter = painterResource(R.drawable.ic_arrow),
                contentDescription = "Arrow",
                tint = colorResource(R.color.setting_icon_color),
                modifier = Modifier
                    .size(24.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}