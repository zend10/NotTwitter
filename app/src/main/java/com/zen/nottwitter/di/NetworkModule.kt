package com.zen.nottwitter.di

import com.zen.nottwitter.data.network.DefaultFirebaseClient
import com.zen.nottwitter.data.network.DefaultFirebaseProvider
import com.zen.nottwitter.data.network.FirebaseClient
import com.zen.nottwitter.data.network.FirebaseProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single<FirebaseClient> { DefaultFirebaseClient() }
    single<FirebaseProvider> { DefaultFirebaseProvider(this.androidContext(), get()) }
}