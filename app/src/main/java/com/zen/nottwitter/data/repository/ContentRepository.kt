package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    val newPost: Flow<Post>
    val sendingPost: Flow<Boolean>
    suspend fun createPost(user: User, message: String, imageUriString: String): Post
    suspend fun getPosts(loadNextPage: Boolean = false): List<Post>
    suspend fun getLocalPosts(): List<Post>
    suspend fun getUserPosts(user: User, loadNextPage: Boolean= false): List<Post>
    suspend fun getLocalUserPosts(user: User): List<Post>
}