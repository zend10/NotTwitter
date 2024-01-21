package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    val newPost: Flow<Post>
    suspend fun createPost(user: User, message: String, imageUriString: String): Post
    suspend fun getPosts(): List<Post>
    suspend fun getLocalPosts(): List<Post>
}