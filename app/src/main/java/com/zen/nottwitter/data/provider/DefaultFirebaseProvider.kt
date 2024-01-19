package com.zen.nottwitter.data.provider

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zen.nottwitter.data.exception.RegisterFailedException
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseClient
import com.zen.nottwitter.domain.TimeUtils
import kotlinx.coroutines.tasks.await

class DefaultFirebaseProvider(private val firebaseClient: FirebaseClient) : FirebaseProvider {

    override suspend fun authenticate() {

    }

    override suspend fun register(nickname: String, email: String, password: String): User {
        try {
            val createdUser = firebaseClient.authClient().createUserWithEmailAndPassword(email, password).await()
            if (createdUser == null || createdUser.user == null)
                throw RegisterFailedException()

            val uid = createdUser.user!!.uid
            val createdOn = TimeUtils.getCurrentMillis()
            val user = mapOf(
                KEY_NICKNAME to nickname,
                KEY_EMAIL to email,
                KEY_CREATED_ON to createdOn
            )

            firebaseClient.firestoreClient().collection(DB_USERS).document(uid).set(user).await()

            return User(uid, nickname, email, createdOn)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun login(email: String, password: String) {

    }

    companion object {
        private const val DB_USERS = "users"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_EMAIL = "email"
        private const val KEY_CREATED_ON = "createdOn"
    }
}