package com.zen.nottwitter.data.provider

import com.google.firebase.firestore.FieldValue
import com.zen.nottwitter.data.exception.LoginFailedException
import com.zen.nottwitter.data.exception.RegisterFailedException
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseClient
import kotlinx.coroutines.tasks.await

class DefaultFirebaseProvider(private val firebaseClient: FirebaseClient) : FirebaseProvider {

    override suspend fun authenticate(): User? {
        try {
            val currentUser = firebaseClient.authClient().currentUser
            return if (currentUser != null) {
                val uid = currentUser.uid
                val storedUser =
                    firebaseClient.firestoreClient().collection(DB_USERS).document(uid).get().await()
                val nickname =
                    storedUser.data?.get(KEY_NICKNAME) as? String ?: throw LoginFailedException()
                val email =
                    storedUser.data?.get(KEY_EMAIL) as? String ?: throw LoginFailedException()
                User(uid, nickname, email)
            } else {
                null
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun register(nickname: String, email: String, password: String): User {
        try {
            val createdUser =
                firebaseClient.authClient().createUserWithEmailAndPassword(email, password).await()

            val uid = createdUser?.user?.uid ?: throw RegisterFailedException()
            val createdOn = FieldValue.serverTimestamp()
            val user = mapOf(
                KEY_NICKNAME to nickname,
                KEY_EMAIL to email,
                KEY_CREATED_ON to createdOn
            )
            firebaseClient.firestoreClient().collection(DB_USERS).document(uid).set(user).await()

            return User(uid, nickname, email)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun login(email: String, password: String): User {
        try {
            val loggedInUser =
                firebaseClient.authClient().signInWithEmailAndPassword(email, password).await()

            val uid = loggedInUser?.user?.uid ?: throw LoginFailedException()
            val storedUser =
                firebaseClient.firestoreClient().collection(DB_USERS).document(uid).get().await()

            val nickname =
                storedUser.data?.get(KEY_NICKNAME) as? String ?: throw LoginFailedException()
            return User(uid, nickname, email)
        } catch (exception: Exception) {
            throw exception
        }
    }

    companion object {
        private const val DB_USERS = "users"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_EMAIL = "email"
        private const val KEY_CREATED_ON = "createdOn"
    }
}