package com.gleb.delineater

import android.app.Application
import com.gleb.delineater.di.menuViewModelModule
import com.gleb.delineater.di.roomModule
import com.gleb.delineater.di.splashViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    companion object{
        const val TABLE_NAME = "imagesTable"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            listOf(
                roomModule,
                splashViewModelModule,
                menuViewModelModule
            )
        }
    }

}