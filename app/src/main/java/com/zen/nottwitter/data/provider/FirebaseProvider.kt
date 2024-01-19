package com.zen.nottwitter.data.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zen.nottwitter.data.model.User

interface FirebaseProvider {
    suspend fun authenticate()
    suspend fun register(nickname: String, email: String, password: String): User
    suspend fun login(email: String, password: String)
}