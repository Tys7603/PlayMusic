package com.example.playmusic.data.parser

import com.example.playmusic.data.model.LyricLine
import com.example.playmusic.data.model.LyricWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

object LyricParser {

    suspend fun parseFromUrl(urlString: String): List<LyricLine> = withContext(Dispatchers.IO) {
        val lines = mutableListOf<LyricLine>()
        try {
            val stream = URL(urlString).openStream()
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(stream)
            doc.documentElement.normalize()

            val paramList = doc.getElementsByTagName("param")
            val rawLinesWords = mutableListOf<List<Pair<String, Long>>>()

            for (i in 0 until paramList.length) {
                val paramNode = paramList.item(i)
                if (paramNode.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                    val paramElem = paramNode as Element
                    val iList = paramElem.getElementsByTagName("i")
                    val wordsInLine = mutableListOf<Pair<String, Long>>()

                    for (j in 0 until iList.length) {
                        val iElem = iList.item(j) as Element
                        val text = iElem.textContent ?: ""
                        val vaAttr = iElem.getAttribute("va")
                        val startTimeSec = vaAttr.toDoubleOrNull() ?: 0.0
                        val startTimeMs = (startTimeSec * 1000).toLong()
                        wordsInLine.add(Pair(text, startTimeMs))
                    }

                    if (wordsInLine.isNotEmpty()) {
                        rawLinesWords.add(wordsInLine)
                    }
                }
            }

            for (lineIdx in 0 until rawLinesWords.size) {
                val currentLineRaw = rawLinesWords[lineIdx]
                val nextLineRaw = rawLinesWords.getOrNull(lineIdx + 1)
                val processedWords = mutableListOf<LyricWord>()

                for (wordIdx in 0 until currentLineRaw.size) {
                    val (wordText, wordStartMs) = currentLineRaw[wordIdx]
                    val nextWordStartMs = if (wordIdx + 1 < currentLineRaw.size) {
                        currentLineRaw[wordIdx + 1].second
                    } else if (nextLineRaw != null && nextLineRaw.isNotEmpty()) {
                        nextLineRaw.first().second
                    } else {
                        wordStartMs + 1500L
                    }

                    val wordEndMs = maxOf(wordStartMs + 100L, nextWordStartMs)
                    processedWords.add(LyricWord(wordText, wordStartMs, wordEndMs))
                }

                if (processedWords.isNotEmpty()) {
                    val lineStartMs = processedWords.first().startTimeMs
                    val lineEndMs = processedWords.last().endTimeMs
                    val fullText = processedWords.joinToString("") { it.text }
                    lines.add(LyricLine(processedWords, lineStartMs, lineEndMs, fullText))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lines
    }
}
