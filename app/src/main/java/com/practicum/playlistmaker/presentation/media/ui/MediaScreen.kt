package com.practicum.playlistmaker.presentation.media.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.CustomTopBarView
import com.practicum.playlistmaker.presentation.media.ui.favorite.FavoriteTracksScreen
import com.practicum.playlistmaker.presentation.media.ui.playlist.PlaylistsScreen
import kotlinx.coroutines.launch

@Composable
fun MediaScreen(
    onCreatePlaylistClick: () -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = pagerState.currentPage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white_black))
    ) {
        CustomTopBarView(56.dp, stringResource(R.string.main_media))

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth(),
            indicator = { tabPositions  ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(horizontal = 16.dp),
                    color = colorResource(R.color.black_white),
                    height = 2.dp
                )
            },
            divider = { }
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                modifier = Modifier.background(colorResource(R.color.backgroundApp)),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text(
                    text = stringResource(R.string.favoriteTrackTab),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontWeight = FontWeight(500),
                    color = colorResource(R.color.black_white)
                ) },

            )

            Tab(
                selected = selectedTabIndex == 1,
                modifier = Modifier.background(colorResource(R.color.backgroundApp)),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text(
                    text = stringResource(R.string.playlistTab),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontWeight = FontWeight(500),
                    color = colorResource(R.color.black_white)
                ) },
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when(page) {
                0 -> FavoriteTracksScreen(navController)
                1 -> PlaylistsScreen(modifier = Modifier, onCreatePlaylistClick, onPlaylistClick)
            }
        }
    }
}

