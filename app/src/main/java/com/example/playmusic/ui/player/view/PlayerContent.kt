package com.example.playmusic.ui.player.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playmusic.R
import com.example.playmusic.data.model.LyricLine
import com.example.playmusic.data.model.LyricWord
import com.example.playmusic.ui.components.KaraokeCanvas
import com.example.playmusic.ui.components.PlayerControls
import com.example.playmusic.ui.player.state.PlayerScreenState
import com.example.playmusic.ui.theme.PlayMusicTheme

@Composable
fun PlayerContent(
    state: PlayerScreenState,
    onOpenFullLyric: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6B8EFF),
            Color(0xFFB197F4),
            Color(0xFFF7B7DF)
        ),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        if (state.isLoading && state.lyricLines.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Đang tải bài hát và lời...",
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Về đâu mái tóc người thương",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Quang Lê",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.img_music),
                    contentDescription = null,
                    modifier = Modifier.size(260.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenFullLyric() }
                ) {
                    KaraokeCanvas(
                        lyricLines = state.lyricLines,
                        currentPositionMs = state.currentPositionMs
                    )
                }

                PlayerControls(
                    state = state,
                    onPlayPauseClick = onPlayPauseClick,
                    onSeekTo = onSeekTo
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerContentPreview() {
    PlayMusicTheme {
        PlayerContent(
            state = PlayerScreenState(
                isPlaying = true,
                currentPositionMs = 36000L,
                durationMs = 240000L,
                isLoading = false,
                lyricLines = listOf(
                    LyricLine(
                        words = listOf(
                            LyricWord("Hồn ", 35144L, 35587L),
                            LyricWord("lỡ ", 35587L, 36006L),
                            LyricWord("sa ", 36006L, 36475L),
                            LyricWord("vào ", 36475L, 36972L),
                            LyricWord("đôi ", 36972L, 37495L),
                            LyricWord("mắt ", 37495L, 37939L),
                            LyricWord("em", 37939L, 38500L)
                        ),
                        startTimeMs = 35144L,
                        endTimeMs = 38500L,
                        fullText = "Hồn lỡ sa vào đôi mắt em"
                    ),
                    LyricLine(
                        words = listOf(
                            LyricWord("Chiều ", 42641L, 43085L),
                            LyricWord("nao ", 43085L, 45485L),
                            LyricWord("xõa ", 45485L, 45486L),
                            LyricWord("tóc ", 45486L, 45487L),
                            LyricWord("ngồi ", 45487L, 45488L),
                            LyricWord("bên ", 45488L, 45489L),
                            LyricWord("rèm", 45489L, 46000L)
                        ),
                        startTimeMs = 42641L,
                        endTimeMs = 46000L,
                        fullText = "Chiều nao xõa tóc ngồi bên rèm"
                    )
                )
            ),
            onOpenFullLyric = {},
            onPlayPauseClick = {},
            onSeekTo = {}
        )
    }
}
