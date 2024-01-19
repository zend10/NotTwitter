package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.User

interface UserRepository {
    suspend fun register(nickname: String, email: String, password: String): User
    suspend fun login(email: String, password: String): User
}