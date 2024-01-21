package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseProvider

class DefaultContentRepository(
    private val firebaseProvider: FirebaseProvider,
    private val localStorageProvider: LocalStorageProvider
) : ContentRepository {

    override suspend fun createPost(user: User, message: String, imageUriString: String): Post {
        try {
            return firebaseProvider.createPost(user, message, imageUriString)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getPosts(): List<Post> {
        try {
            val posts = firebaseProvider.getPosts()
            localStorageProvider.deletePosts()
            localStorageProvider.savePosts(posts)
            return posts
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getLocalPosts(): List<Post> {
        return localStorageProvider.getPosts()
    }
}