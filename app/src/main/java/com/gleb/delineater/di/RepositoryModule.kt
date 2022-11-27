package com.gleb.delineater.di

import com.gleb.delineater.data.repositories.PictureRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { PictureRepositoryImpl(pictureDao = get()) }
}