package com.example.playmusic.data.model

data class LyricWord(
    val text: String,
    val startTimeMs: Long,
    val endTimeMs: Long
)

data class LyricLine(
    val words: List<LyricWord>,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val fullText: String
)
