package com.practicum.playlistmaker.presentation.media.ui.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.NotFoundMessage
import com.practicum.playlistmaker.presentation.media.state.FavouriteTrackViewState
import com.practicum.playlistmaker.presentation.media.viewmodel.FavoriteTracksViewModel
import com.practicum.playlistmaker.presentation.player.ui.PlayerFragment
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteTracksScreen(
    navController: NavController
) {
    val trackFavoriteVM: FavoriteTracksViewModel = koinViewModel()
    val state =  trackFavoriteVM.stateFavourite.observeAsState().value

    FavoriteTracksContent(
        state = state ?: FavouriteTrackViewState.Empty,
        onTrackClick = { track ->
            transferTrackToPlayer(track, navController)
        },
        onRefresh = { trackFavoriteVM.getListFavourite() }
    )
}

@Composable
private fun FavoriteTracksContent(
    state: FavouriteTrackViewState,
    onTrackClick: (Track) -> Unit,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is FavouriteTrackViewState.Empty ->{

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.pad_128dp)))
                EmptyFavoriteList()
            }
            is FavouriteTrackViewState.Content -> TrackList(
                tracks = state.favoriteList,
                onTrackClick = onTrackClick
            )
        }
    }

    LaunchedEffect(Unit) {
        onRefresh()
    }
}

@Composable
private fun TrackList(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        items(tracks) { item ->
            com.practicum.playlistmaker.presentation.search.ui.ListItem(
                trackName = item.trackName ?: "",
                artistName = item.artistName ?: "",
                trackTime = item.formattedTime,
                coverUrl = item.artworkUrl100 ?: "",
                onItemClick = {
                    onTrackClick(item)
                }
            )
        }
    }
}



@Composable
private fun EmptyFavoriteList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Spacer(modifier = Modifier.height(500.dp))

        NotFoundMessage(stringResource(R.string.mediaFavoriteHolder))
    }
}

private fun transferTrackToPlayer(track: Track, navController: NavController) {
    navController.navigate(R.id.action_mediaFragment_to_playerFragment, PlayerFragment.createArgs(track))
}
