package com.zen.nottwitter.di

import com.zen.nottwitter.data.localstorage.DefaultLocalStorageProvider
import com.zen.nottwitter.data.localstorage.DefaultRealmClient
import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.localstorage.RealmClient
import com.zen.nottwitter.data.model.entity.PostEntity
import com.zen.nottwitter.data.model.entity.UserEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val localStorageModule = module {
    single<RealmClient> {
        val configuration =
            RealmConfiguration.create(schema = setOf(UserEntity::class, PostEntity::class))
        val realm = Realm.open(configuration)
        DefaultRealmClient(realm)
    }
    single<LocalStorageProvider> { DefaultLocalStorageProvider(get()) }
}