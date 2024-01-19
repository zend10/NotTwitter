package com.zen.nottwitter.data.localstorage

import com.zen.nottwitter.data.model.User

interface LocalStorageProvider {
    suspend fun getUser(): User
    suspend fun saveUser(user: User): User
    suspend fun deleteUser()
}