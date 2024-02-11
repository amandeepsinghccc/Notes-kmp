package org.co.notes

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppClass: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@AppClass)
            androidLogger()
            modules()
        }
    }
}