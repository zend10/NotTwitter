package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultContentRepository(
    private val firebaseProvider: FirebaseProvider,
    private val localStorageProvider: LocalStorageProvider
) : ContentRepository {

    private val _newPost = MutableSharedFlow<Post>()
    override val newPost: Flow<Post>
        get() = _newPost

    private val _sendingPost = MutableStateFlow(false)
    override val sendingPost: Flow<Boolean>
        get() = _sendingPost

    override suspend fun createPost(user: User, message: String, imageUriString: String): Post {
        try {
            _sendingPost.emit(true)
            val post = firebaseProvider.createPost(user, message, imageUriString)
            _newPost.emit(post)
            return post
        } catch (exception: Exception) {
            throw exception
        } finally {
            _sendingPost.emit(false)
        }
    }

    override suspend fun getPosts(loadNextPage: Boolean): List<Post> {
        try {
            val posts = firebaseProvider.getPosts(loadNextPage)
            if (!loadNextPage) {
                localStorageProvider.deletePosts()
                localStorageProvider.savePosts(posts)
            }
            return posts
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getLocalPosts(): List<Post> {
        return localStorageProvider.getPosts()
    }

    override suspend fun getUserPosts(user: User, loadNextPage: Boolean): List<Post> {
        try {
            val posts = firebaseProvider.getUserPosts(user, loadNextPage)
            if (!loadNextPage) {
                localStorageProvider.deleteUserPosts()
                localStorageProvider.saveUserPosts(posts)
            }
            return posts
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getLocalUserPosts(user: User): List<Post> {
        return localStorageProvider.getUserPosts()
    }
}