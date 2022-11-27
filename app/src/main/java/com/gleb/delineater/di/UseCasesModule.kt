package com.gleb.delineater.di

import com.gleb.delineater.domain.usecases.NewPictureUseCase
import com.gleb.delineater.domain.usecases.UpdatePictureUseCase
import org.koin.dsl.module

val useCasesModule = module {

    factory { NewPictureUseCase(pictureRepository = get()) }
    factory { UpdatePictureUseCase(pictureRepository = get()) }

}