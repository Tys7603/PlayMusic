package com.example.playmusic.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.playmusic.ui.player.view.FullLyricContent
import com.example.playmusic.ui.player.view.PlayerContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isFullLyricVisible) {
        FullLyricContent(
            state = uiState,
            onClose = { viewModel.closeFullLyric() },
            onPlayPauseClick = { viewModel.togglePlayPause() },
            onSeekTo = { pos -> viewModel.seekTo(pos) },
            modifier = modifier
        )
    } else {
        PlayerContent(
            state = uiState,
            onOpenFullLyric = { viewModel.openFullLyric() },
            onPlayPauseClick = { viewModel.togglePlayPause() },
            onSeekTo = { pos -> viewModel.seekTo(pos) },
            modifier = modifier
        )
    }
}
