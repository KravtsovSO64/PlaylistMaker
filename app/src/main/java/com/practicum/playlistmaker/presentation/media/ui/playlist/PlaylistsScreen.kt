package com.practicum.playlistmaker.presentation.media.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.media.NotFoundMessage
import com.practicum.playlistmaker.presentation.media.state.PlaylistViewState
import com.practicum.playlistmaker.presentation.media.viewmodel.PlaylistViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    onCreatePlaylistClick: () -> Unit,
    onPlaylistClick: (Playlist) -> Unit
) {
    val playlistVM: PlaylistViewModel = koinViewModel()
    val state by playlistVM.stateView.observeAsState(PlaylistViewState.Empty(false))

    LaunchedEffect(Unit) {
        playlistVM.getPlaylists()
    }

    PlaylistsContent(
        state = state,
        modifier = modifier,
        onCreatePlaylistClick = onCreatePlaylistClick,
        onPlaylistClick = onPlaylistClick
    )
}

@Composable
fun PlaylistsContent(
    state: PlaylistViewState,
    modifier: Modifier = Modifier,
    onCreatePlaylistClick: () -> Unit,
    onPlaylistClick: (Playlist) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.backgroundApp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
                ) {
                CreatePlaylistButton(
                    onClick = onCreatePlaylistClick,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(vertical = dimensionResource(R.dimen.pad_24dp))
                )
            }
            when (state) {
                is PlaylistViewState.Empty -> {
                    if (state.enableErrorMessage) {
                        NotFoundMessage(stringResource(R.string.mediaPlaylistHolder))
                    }
                }
                is PlaylistViewState.Content -> {
                    if (state.enableErrorMessage) {
                        NotFoundMessage(stringResource(R.string.mediaPlaylistHolder))
                    } else {
                        PlaylistsGrid(
                            playlists = state.playlist,
                            onItemClick = onPlaylistClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreatePlaylistButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.setting_header_text),
            contentColor = colorResource(R.color.update_button)
        ),
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.pad_8dp))
    ) {
        Text(
            text = stringResource(R.string.buttonCreatePlaylist),
            fontSize = 14.sp,
            letterSpacing = 0.sp,
        )
    }
}

@Composable
fun PlaylistsGrid(
    playlists: List<Playlist>,
    onItemClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.pad_8dp)),
        contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.pad_8dp))
    ) {
        items(playlists) { playlist ->
            PlaylistItem(playlist = playlist, onClick = { onItemClick(playlist) })
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            AsyncImage(
                model = playlist.coverImagePath,
                contentDescription = "Playlist cover",
                placeholder = painterResource(R.drawable.ic_place_holder),
                error = painterResource(R.drawable.ic_place_holder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.backgroundApp))
            )
        }

        Text(
            text = playlist.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            color = colorResource(R.color.black_white),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = getStringFrom(playlist.trackCount),
            modifier = Modifier.fillMaxWidth(),
            color = colorResource(R.color.black_white),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


private fun getStringFrom(quantity: Int): String {
    val mod10 = quantity % 10
    val mod100 = quantity % 100

    return when {
        mod10 == 1 && mod100 != 11 -> "$quantity трек"
        mod10 in 2..4 && mod100 !in 12..14 -> "$quantity трека"
        else -> "$quantity треков"
    }
}
