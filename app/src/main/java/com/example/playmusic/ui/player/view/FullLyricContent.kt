package com.example.playmusic.ui.player.view

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playmusic.R
import com.example.playmusic.data.model.LyricLine
import com.example.playmusic.ui.components.PlayerControls
import com.example.playmusic.ui.player.state.PlayerScreenState

@Composable
fun FullLyricContent(
    state: PlayerScreenState,
    onClose: () -> Unit,
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

    val currentLineIndex = state.lyricLines.indexOfFirst { line ->
        state.currentPositionMs in line.startTimeMs..line.endTimeMs
    }

    val listState = rememberLazyListState()

    LaunchedEffect(currentLineIndex, listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && currentLineIndex >= 0) {
            listState.animateScrollToItem(
                index = currentLineIndex,
                scrollOffset = -220
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBrush)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { onClose() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow),
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Về đâu mái tóc người thương",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Quang Lê",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(state.lyricLines) { index, line ->
                            val isActive = (index == currentLineIndex)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                                    .clickable { onSeekTo(line.startTimeMs) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isActive) {
                                    ActiveLyricLineItem(
                                        line = line,
                                        currentPositionMs = state.currentPositionMs
                                    )
                                } else {
                                    Text(
                                        text = line.fullText,
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PlayerControls(
                        state = state,
                        onPlayPauseClick = onPlayPauseClick,
                        onSeekTo = onSeekTo
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveLyricLineItem(
    line: LyricLine,
    currentPositionMs: Long,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
    ) {
        val width = size.width
        val height = size.height

        val textSizePx = 16.sp.toPx()

        val basePaint = Paint().apply {
            isAntiAlias = true
            textSize = textSizePx
            color = Color.White.toArgb()
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val highlightPaint = Paint().apply {
            isAntiAlias = true
            textSize = textSizePx
            color = Color(0xFF1E1E1E).toArgb()
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val measurePaint = Paint(basePaint).apply {
            textAlign = Paint.Align.LEFT
        }

        drawContext.canvas.nativeCanvas.apply {
            val centerY = height * 0.7f
            val fullText = line.fullText
            val actualLineWidth = basePaint.measureText(fullText)
            val lineStartX = (width - actualLineWidth) / 2f

            drawText(fullText, width / 2f, centerY, basePaint)

            var highlightedWidth = 0f
            for (word in line.words) {
                val wordWidth = measurePaint.measureText(word.text)
                if (currentPositionMs >= word.endTimeMs) {
                    highlightedWidth += wordWidth
                } else if (currentPositionMs > word.startTimeMs) {
                    val duration = (word.endTimeMs - word.startTimeMs).coerceAtLeast(1L)
                    val progress = (currentPositionMs - word.startTimeMs).toFloat() / duration
                    highlightedWidth += (wordWidth * progress.coerceIn(0f, 1f))
                    break
                } else {
                    break
                }
            }

            val clipRight = lineStartX + highlightedWidth
            if (clipRight > lineStartX) {
                save()
                clipRect(lineStartX, 0f, clipRight, height)
                drawText(fullText, width / 2f, centerY, highlightPaint)
                restore()
            }
        }
    }
}
