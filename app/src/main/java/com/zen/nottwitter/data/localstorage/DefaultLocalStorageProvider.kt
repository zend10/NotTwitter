package com.zen.nottwitter.data.localstorage

import com.zen.nottwitter.data.mapper.UserEntityMapper
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.model.entity.UserEntity
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

class DefaultLocalStorageProvider(private val realmClient: RealmClient) : LocalStorageProvider {

    override suspend fun getUser(): User {
        try {
            return realmClient.realmClient().query<UserEntity>().find().first().run {
                UserEntityMapper().mapTo(this)
            }
        } catch (exception: NoSuchElementException) {
            throw exception
        }
    }

    override suspend fun saveUser(user: User): User {
        return realmClient.realmClient()
            .write { copyToRealm(UserEntityMapper().mapFrom(user), UpdatePolicy.ALL) }.run { user }
    }

    override suspend fun deleteUser() {
        realmClient.realmClient().write {
            val query = this.query<UserEntity>().find()
            query.firstOrNull()?.let { delete(it) }
        }
    }
}