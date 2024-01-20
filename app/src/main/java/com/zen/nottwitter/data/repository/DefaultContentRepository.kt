package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseProvider

class DefaultContentRepository(private val firebaseProvider: FirebaseProvider) : ContentRepository {

    override suspend fun createPost(user: User, message: String, imageUriString: String): Post {
        try {
            return firebaseProvider.createPost(user, message, imageUriString)
        } catch (exception: Exception) {
            throw exception
        }
    }
}