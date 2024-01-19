package com.zen.nottwitter

import android.app.Application
import com.zen.nottwitter.di.dataModule
import com.zen.nottwitter.di.featureModule
import com.zen.nottwitter.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(networkModule, dataModule, featureModule)
        }
    }
}