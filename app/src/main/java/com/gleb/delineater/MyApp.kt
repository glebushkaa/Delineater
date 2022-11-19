package com.gleb.delineater

import android.app.Application
import com.gleb.delineater.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(provideModules())
        }
    }

    private fun provideModules() = listOf(
        viewModelsModule,
        databaseModule,
        repositoryModule
    )

}