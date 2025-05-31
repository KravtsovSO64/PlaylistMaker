package com.practicum.playlistmaker.presentation.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.CustomTopBarView
import com.practicum.playlistmaker.presentation.search.state.State
import com.practicum.playlistmaker.presentation.search.viewmodel.TrackSearchViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchScreen(onTrackClick: (Track) -> Unit) {
    val trackSearchVM: TrackSearchViewModel = koinViewModel()
    val text =  trackSearchVM.searchText.collectAsState().value
    val state  = trackSearchVM.state.observeAsState().value

    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        trackSearchVM.getListHistorySearchMusic()
    }
    Column(
        modifier = Modifier
            .background(color = colorResource(R.color.backgroundApp))
            .fillMaxSize()
    ) {

        CustomTopBarView(height = 56.dp, title = stringResource(R.string.search))

        SearchTextField(
            text = text,
            onTextChange = { trackSearchVM.onTextChanged(it) },
            isFocused = isFocused,
            onFocusChange = { isFocused = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        state?.let { StateScreen(it, isFocused, trackSearchVM, text, onTrackClick) }
    }

}

@Composable
fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(horizontal = 16.dp)
            .background(
                color = colorResource(R.color.light_gray_white),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = colorResource(R.color.gray_black),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { onFocusChange(it.isFocused) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = colorResource(R.color.black),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular))
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (text.isEmpty() && !isFocused) {
                                Text(
                                    text = stringResource(R.string.search),
                                    color = colorResource(R.color.gray_black),
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    },
                    cursorBrush = SolidColor(colorResource(R.color.blue))
                )
            }

            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_clear_text),
                    contentDescription = null,
                    tint = colorResource(R.color.gray_black),
                    modifier = Modifier
                        .size(12.dp)
                        .clickable { onTextChange("") }
                )
            }
        }
    }
}

@Composable
fun StateScreen(
    state: State,
    isFocused: Boolean,
    trackSearchVM: TrackSearchViewModel,
    text: String,
    onTrackClick: (Track) -> Unit
) {
    when (state) {
        is State.Content -> {
            ListTracks(state.tracks, trackSearchVM, onTrackClick)
        }
        is State.History -> {
            if (isFocused && state.tracks.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.hintMessage),
                        textAlign = TextAlign.Center,
                        fontSize = 19.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontWeight = FontWeight(500),
                        color = colorResource(R.color.black_white),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(modifier = Modifier.wrapContentHeight()) {
                        ListTracks(state.tracks, trackSearchVM, onTrackClick)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { trackSearchVM.removeListHistorySearchMusic() },
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.black_white)),
                    ) {
                        Text(
                            text = stringResource(R.string.buttonClearHistory),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = colorResource(R.color.white_black)
                        )
                    }
                }
            }
        }
        is State.Error -> {
            CustomNotification(
                messageText = stringResource(R.string.noInternetСontent),
                imageResId = R.drawable.ic_no_internet,
                showButton = true,
                buttonText = stringResource(R.string.buttonUpdateSearch),
                onButtonClick = {
                    trackSearchVM.searchMusic(text)
                }
            )
        }
        is State.Empty -> {
            CustomNotification(
                messageText = stringResource(R.string.noFoundСontent),
                imageResId = R.drawable.ic_not_found
            )
        }
        is State.Loading -> {
            LoadingIndicator(true)
        }
    }
}

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

@Composable
fun ListTracks(
    list: List<Track>,
    trackSearchVM: TrackSearchViewModel,
    onTrackClick: (Track) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(list) { item ->
            ListItem(
                trackName = item.trackName ?: "",
                artistName = item.artistName ?: "",
                trackTime = item.formattedTime,
                coverUrl = item.artworkUrl100 ?: "",
                onItemClick = {
                    trackSearchVM.setToListHistorySearchMusic(item)
                    onTrackClick(item)
                }
            )
        }
    }
}

@Composable
fun LoadingIndicator(
    visible: Boolean
) {
    if (visible) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = colorResource(R.color.blue),
                strokeWidth = 4.dp
            )
        }
    }
}

@Composable
fun CustomNotification(
    modifier: Modifier = Modifier,
    messageText: String,
    imageResId: Int? = null,
    imageModifier: Modifier = Modifier.size(120.dp),
    showButton: Boolean = false,
    buttonText: String = "",
    onButtonClick: () -> Unit = {},
    textFontFamily: FontFamily = FontFamily(Font(R.font.ys_display_medium)),
    textAlign: TextAlign = TextAlign.Center,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = colorResource(R.color.setting_header_text),
        contentColor = colorResource(R.color.update_button)
    ),
    buttonContentPadding: PaddingValues = PaddingValues(6.dp),
    buttonTextSize: TextUnit = 14.sp,
    spacingBetweenItems: Dp = 16.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement
    ) {
        imageResId?.let {
            Image(
                modifier = imageModifier,
                painter = painterResource(it),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(spacingBetweenItems))
        }

        Text(
            text = messageText,
            fontFamily = textFontFamily,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )

        if (showButton) {
            Spacer(modifier = Modifier.height(spacingBetweenItems))

            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .size(width = 92.dp, height = 36.dp),
                colors = buttonColors,
                contentPadding = buttonContentPadding
            ) {
                Text(
                    text = buttonText,
                    fontSize = buttonTextSize,
                )
            }
        }
    }
}