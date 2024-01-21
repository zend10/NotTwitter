package com.zen.nottwitter.data.localstorage

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User

interface LocalStorageProvider {
    suspend fun getUser(): User
    suspend fun saveUser(user: User): User
    suspend fun deleteUser()
    suspend fun getPosts(): List<Post>
    suspend fun savePosts(posts: List<Post>): List<Post>
    suspend fun deletePosts()
    suspend fun getUserPosts(): List<Post>
    suspend fun saveUserPosts(posts: List<Post>): List<Post>
    suspend fun deleteUserPosts()
}