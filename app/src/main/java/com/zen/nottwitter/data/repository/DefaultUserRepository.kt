package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultUserRepository(
    private val firebaseProvider: FirebaseProvider,
    private val localStorageProvider: LocalStorageProvider
) : UserRepository {

    private val _user = MutableStateFlow(User())
    override val user: Flow<User>
        get() = _user

    private val _logoutTrigger = MutableSharedFlow<Unit>()
    override val logoutTrigger: Flow<Unit>
        get() = _logoutTrigger

    override suspend fun authenticate(): User? {
        try {
            val user = firebaseProvider.authenticate()
            if (user != null) {
                localStorageProvider.deleteUser()
                localStorageProvider.saveUser(user)
                _user.emit(user)
            }
            return user
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun register(nickname: String, email: String, password: String): User {
        try {
            val user = firebaseProvider.register(nickname, email, password)
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(user)
            _user.emit(user)
            return user
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun login(email: String, password: String): User {
        try {
            val user = firebaseProvider.login(email, password)
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(user)
            _user.emit(user)
            return user
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun logout() {
        localStorageProvider.deleteUser()
        localStorageProvider.deleteUserPosts()
        firebaseProvider.logout()
        _logoutTrigger.emit(Unit)
    }

    override suspend fun getUser(): User {
        try {
            return localStorageProvider.getUser()
        } catch (exception: NoSuchElementException) {
            throw exception
        }
    }
}