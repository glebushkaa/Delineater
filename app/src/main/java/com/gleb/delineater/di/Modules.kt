package com.gleb.delineater.di

import android.content.Context
import androidx.room.Room
import com.gleb.delineater.data.repositories.PictureRepository
import com.gleb.delineater.data.room.PictureDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val TABLE_NAME = "images_table"

val databaseModule = module {

    fun buildPictureDatabase(context: Context) = Room.databaseBuilder(
        context, PictureDatabase::class.java, TABLE_NAME
    ).build()

    fun providePictureDao(pictureDatabase: PictureDatabase) = pictureDatabase.pictureDao()

    single { buildPictureDatabase(context = androidContext()) }
    single { providePictureDao(pictureDatabase = get()) }

}

val repositoryModule = module {
    single { PictureRepository(pictureDao = get()) }
}
