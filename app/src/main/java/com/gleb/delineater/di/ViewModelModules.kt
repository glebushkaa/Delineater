package com.gleb.delineater.di

import com.gleb.delineater.ui.viewModels.PictureMenuViewModel
import com.gleb.delineater.ui.viewModels.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val splashViewModelModule = module {
    viewModel {
        SplashViewModel(
            pictureDao = get()
        )
    }
}

val menuViewModelModule = module {
    viewModel {
        PictureMenuViewModel()
    }
}