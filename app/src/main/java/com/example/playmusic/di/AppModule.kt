package com.example.playmusic.di

import com.example.playmusic.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PlayerViewModel(get()) }
}
