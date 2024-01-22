package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<User>
    val logoutTrigger: Flow<Unit>

    suspend fun authenticate(): User?
    suspend fun register(nickname: String, email: String, password: String): User
    suspend fun login(email: String, password: String): User
    suspend fun logout()

    @Throws(NoSuchElementException::class)
    suspend fun getUser(): User
}