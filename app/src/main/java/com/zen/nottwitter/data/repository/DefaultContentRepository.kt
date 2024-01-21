package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class DefaultContentRepository(
    private val firebaseProvider: FirebaseProvider,
    private val localStorageProvider: LocalStorageProvider
) : ContentRepository {

    private val _newPost = MutableSharedFlow<Post>()
    override val newPost: Flow<Post>
        get() = _newPost

    override suspend fun createPost(user: User, message: String, imageUriString: String): Post {
        try {
            val post = firebaseProvider.createPost(user, message, imageUriString)
            _newPost.emit(post)
            return post
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