package com.zen.nottwitter.data.localstorage

import io.realm.kotlin.Realm

class DefaultRealmClient(private val realm: Realm) : RealmClient {

    override fun realmClient(): Realm {
        return realm
    }
}