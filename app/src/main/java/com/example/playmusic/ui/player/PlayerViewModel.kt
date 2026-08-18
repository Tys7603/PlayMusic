package com.example.playmusic.ui.player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.playmusic.data.parser.LyricParser
import com.example.playmusic.ui.player.state.PlayerScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(context: Context) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerScreenState())
    val uiState: StateFlow<PlayerScreenState> = _uiState.asStateFlow()

    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context.applicationContext).build().apply {
        val mediaItem = MediaItem.fromUri("https://storage.googleapis.com/ikara-storage/tmp/beat.mp3")
        setMediaItem(mediaItem)
        prepare()
    }

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _uiState.update {
                        it.copy(
                            durationMs = exoPlayer.duration.coerceAtLeast(0L),
                            isLoading = false
                        )
                    }
                }
            }
        })

        loadLyrics()
        startPositionUpdates()
    }

    private fun loadLyrics() {
        viewModelScope.launch {
            val lines = LyricParser.parseFromUrl("https://storage.googleapis.com/ikara-storage/ikara/lyrics.xml")
            _uiState.update { it.copy(lyricLines = lines) }
        }
    }

    private fun startPositionUpdates() {
        viewModelScope.launch {
            while (isActive) {
                if (exoPlayer.isPlaying) {
                    val pos = exoPlayer.currentPosition.coerceAtLeast(0L)
                    val dur = if (_uiState.value.durationMs <= 0L && exoPlayer.duration > 0L) {
                        exoPlayer.duration
                    } else {
                        _uiState.value.durationMs
                    }
                    _uiState.update {
                        it.copy(
                            currentPositionMs = pos,
                            durationMs = dur
                        )
                    }
                }
                delay(16)
            }
        }
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
        _uiState.update { it.copy(currentPositionMs = positionMs) }
    }

    fun openFullLyric() {
        _uiState.update { it.copy(isFullLyricVisible = true) }
    }

    fun closeFullLyric() {
        _uiState.update { it.copy(isFullLyricVisible = false) }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
