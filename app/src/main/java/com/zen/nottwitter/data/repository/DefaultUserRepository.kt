package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseProvider

class DefaultUserRepository(
    private val firebaseProvider: FirebaseProvider,
    private val localStorageProvider: LocalStorageProvider
) : UserRepository {

    override suspend fun authenticate(): User? {
        try {
            val user = firebaseProvider.authenticate()
            if (user != null) {
                localStorageProvider.saveUser(user)
            }
            return user
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun register(nickname: String, email: String, password: String): User {
        try {
            val user = firebaseProvider.register(nickname, email, password)
            localStorageProvider.saveUser(user)
            return user
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun login(email: String, password: String): User {
        try {
            val user = firebaseProvider.login(email, password)
            localStorageProvider.saveUser(user)
            return user
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun logout() {
        localStorageProvider.deleteUser()
        firebaseProvider.logout()
    }

    override suspend fun getUser(): User {
        try {
            return localStorageProvider.getUser()
        } catch (exception: NoSuchElementException) {
            throw exception
        }
    }
}