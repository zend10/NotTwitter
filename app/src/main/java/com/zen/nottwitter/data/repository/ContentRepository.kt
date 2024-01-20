package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User

interface ContentRepository {
    suspend fun createPost(user: User, message: String, imageUriString: String): Post
}