package com.example.githubbrowserwithapollo

import android.app.Application
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
    }
}