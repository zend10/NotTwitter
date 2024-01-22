package com.zen.nottwitter.data.network

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User

interface FirebaseProvider {
    suspend fun authenticate(): User?
    suspend fun register(nickname: String, email: String, password: String): User
    suspend fun login(email: String, password: String): User
    suspend fun logout()
    suspend fun createPost(user: User, message: String, imageUriString: String): Post
    suspend fun getPosts(loadNextPage: Boolean = false): List<Post>
    suspend fun getUserPosts(user: User, loadNextPage: Boolean = false): List<Post>
}