package com.example.githubbrowserwithapollo

import android.app.Application
import com.example.githubbrowserwithapollo.di.apiModule
import com.example.githubbrowserwithapollo.di.repositoryModule
import com.example.githubbrowserwithapollo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@App)
            modules(apiModule, repositoryModule, viewModelModule)
        }
    }
}