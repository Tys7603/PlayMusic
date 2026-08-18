package com.example.playmusic.ui.player.state

import com.example.playmusic.data.model.LyricLine

data class PlayerScreenState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val lyricLines: List<LyricLine> = emptyList(),
    val isLoading: Boolean = true,
    val isFullLyricVisible: Boolean = false
)
