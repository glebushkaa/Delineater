package com.gleb.delineater.di

import com.gleb.delineater.ui.viewModels.DownloadViewModel
import com.gleb.delineater.ui.viewModels.DrawViewModel
import com.gleb.delineater.ui.viewModels.MenuViewModel
import com.gleb.delineater.ui.viewModels.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelsModule = module {
    viewModel {
        SplashViewModel(pictureRepository = get())
    }
    viewModel {
        MenuViewModel(pictureRepository = get())
    }
    viewModel {
        DrawViewModel()
    }
    viewModel {
        DownloadViewModel()
    }
}
