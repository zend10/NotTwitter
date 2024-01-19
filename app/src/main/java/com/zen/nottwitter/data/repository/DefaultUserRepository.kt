package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.provider.FirebaseProvider

class DefaultUserRepository(private val firebaseProvider: FirebaseProvider) : UserRepository {

    override suspend fun register(nickname: String, email: String, password: String): User {
        try {
            val user = firebaseProvider.register(nickname, email, password)
            // save to DB
            return user
        } catch (exception: Exception) {
            throw exception
        }
    }
}