package com.zen.nottwitter.data.localstorage

import io.realm.kotlin.Realm

interface RealmClient {
    fun realmClient(): Realm
}