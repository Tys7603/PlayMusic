package com.example.playmusic.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playmusic.data.model.LyricLine

@Composable
fun KaraokeCanvas(
    lyricLines: List<LyricLine>,
    currentPositionMs: Long,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val width = size.width
        val height = size.height

        if (lyricLines.isEmpty()) {
            return@Canvas
        }

        var currentLineIndex = lyricLines.indexOfFirst { line ->
            currentPositionMs in line.startTimeMs..line.endTimeMs
        }

        if (currentLineIndex == -1) {
            val nextIndex = lyricLines.indexOfFirst { it.startTimeMs > currentPositionMs }
            currentLineIndex = if (nextIndex > 0) nextIndex - 1 else if (nextIndex == 0) 0 else lyricLines.lastIndex
        }

        val isTopActive = (currentLineIndex % 2 == 0)

        val topLine: LyricLine? = if (isTopActive) {
            lyricLines.getOrNull(currentLineIndex)
        } else {
            lyricLines.getOrNull(currentLineIndex + 1)
        }

        val bottomLine: LyricLine? = if (isTopActive) {
            lyricLines.getOrNull(currentLineIndex + 1)
        } else {
            lyricLines.getOrNull(currentLineIndex)
        }

        val activeLine = lyricLines.getOrNull(currentLineIndex)

        val textSizePx = 16.sp.toPx()
        val maxAvailableWidth = (width - 24.dp.toPx()).toInt().coerceAtLeast(100)

        val baseTextPaint = TextPaint().apply {
            isAntiAlias = true
            textSize = textSizePx
            color = Color.White.toArgb()
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val highlightTextPaint = TextPaint().apply {
            isAntiAlias = true
            textSize = textSizePx
            color = Color(0xFF1E1E1E).toArgb()
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val measurePaint = Paint(baseTextPaint).apply {
            textAlign = Paint.Align.LEFT
        }

        fun createStaticLayout(text: String, paint: TextPaint): StaticLayout {
            return StaticLayout.Builder.obtain(text, 0, text.length, paint, maxAvailableWidth)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setLineSpacing(0f, 1.0f)
                .build()
        }

        drawContext.canvas.nativeCanvas.apply {
            val topY = height * 0.30f
            val bottomY = height * 0.75f

            fun renderLine(
                line: LyricLine,
                centerY: Float,
                isActive: Boolean
            ) {
                val fullText = line.fullText
                val baseLayout = createStaticLayout(fullText, baseTextPaint)

                save()
                translate(width / 2f - baseLayout.width / 2f, centerY - baseLayout.height / 2f)
                baseLayout.draw(this)

                if (isActive && activeLine != null) {
                    val highlightLayout = createStaticLayout(fullText, highlightTextPaint)
                    var highlightedWidth = 0f
                    for (word in activeLine.words) {
                        val wWidth = measurePaint.measureText(word.text)
                        if (currentPositionMs >= word.endTimeMs) {
                            highlightedWidth += wWidth
                        } else if (currentPositionMs > word.startTimeMs) {
                            val duration = (word.endTimeMs - word.startTimeMs).coerceAtLeast(1L)
                            val progress = (currentPositionMs - word.startTimeMs).toFloat() / duration
                            highlightedWidth += (wWidth * progress.coerceIn(0f, 1f))
                            break
                        } else {
                            break
                        }
                    }

                    var remainingWidth = highlightedWidth
                    val lineCount = baseLayout.lineCount

                    for (i in 0 until lineCount) {
                        val lineTop = baseLayout.getLineTop(i).toFloat()
                        val lineBottom = baseLayout.getLineBottom(i).toFloat()
                        val lineLeft = baseLayout.getLineLeft(i)
                        val lineRight = baseLayout.getLineRight(i)
                        val lineWidth = lineRight - lineLeft

                        if (remainingWidth > 0f && lineWidth > 0f) {
                            val clipWidth = remainingWidth.coerceAtMost(lineWidth)
                            save()
                            clipRect(lineLeft, lineTop, lineLeft + clipWidth, lineBottom)
                            highlightLayout.draw(this)
                            restore()
                            remainingWidth -= lineWidth
                        }
                    }
                }
                restore()
            }

            if (topLine != null) {
                renderLine(
                    line = topLine,
                    centerY = topY,
                    isActive = isTopActive
                )
            }

            if (bottomLine != null) {
                renderLine(
                    line = bottomLine,
                    centerY = bottomY,
                    isActive = !isTopActive
                )
            }
        }
    }
}
