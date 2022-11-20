package com.gleb.delineater.di

import androidx.room.Room
import com.gleb.delineater.data.repositories.PictureRepository
import com.gleb.delineater.data.room.PictureDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val TABLE_NAME = "images_table"

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(), PictureDatabase::class.java, TABLE_NAME
        ).fallbackToDestructiveMigration().build()
    }
    single { (get() as PictureDatabase).pictureDao() }

}

val repositoryModule = module {
    single { PictureRepository(pictureDao = get()) }
}
