package com.zen.nottwitter

import android.app.Application
import com.zen.nottwitter.di.featureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(featureModule)
        }
    }
}