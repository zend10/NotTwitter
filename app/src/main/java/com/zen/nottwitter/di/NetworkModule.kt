package com.zen.nottwitter.di

import com.zen.nottwitter.data.network.DefaultFirebaseClient
import com.zen.nottwitter.data.network.FirebaseClient
import com.zen.nottwitter.data.provider.DefaultFirebaseProvider
import com.zen.nottwitter.data.provider.FirebaseProvider
import org.koin.dsl.module

val networkModule = module {
    single<FirebaseClient> { DefaultFirebaseClient() }
    single<FirebaseProvider> { DefaultFirebaseProvider(get()) }
}