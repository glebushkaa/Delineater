package com.gleb.delineater

import android.app.Application
import com.gleb.delineater.di.menuViewModelModule
import com.gleb.delineater.di.splashViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            listOf(
                splashViewModelModule,
                menuViewModelModule
            )
        }
    }

}