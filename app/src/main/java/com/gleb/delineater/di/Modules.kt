package com.gleb.delineater.di

import android.content.Context
import android.provider.MediaStore
import androidx.room.Room
import com.gleb.delineater.data.FileHelper
import com.gleb.delineater.data.MediaHelper
import com.gleb.delineater.data.repositories.PictureRepository
import com.gleb.delineater.data.room.PictureDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val TABLE_NAME = "images_table"

val roomModule = module {

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

val helpersModule = module {
    factory { FileHelper() }
    factory { MediaHelper(androidContext()) }
}
