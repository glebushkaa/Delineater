package com.gleb.delineater.di

import com.gleb.delineater.ui.viewModels.DownloadViewModel
import com.gleb.delineater.ui.viewModels.DrawViewModel
import com.gleb.delineater.ui.viewModels.MenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelsModule = module {
    viewModel {
        MenuViewModel(
            allPicturesUseCase = get(),
            deletePictureUseCase = get()
        )
    }
    viewModel {
        DrawViewModel(
            updatePictureUseCase = get(),
            newPictureUseCase = get()
        )
    }
    viewModel {
        DownloadViewModel(
            newPictureUseCase = get(),
            updatePictureUseCase = get()
        )
    }
}
