package com.zen.nottwitter.domain.usecase

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository

class CreatePostUseCase(
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository
) : UseCase<CreatePostRequest, Post> {

    override suspend fun execute(request: CreatePostRequest): Post {
        try {
            val user = userRepository.getUser()
            return contentRepository.createPost(user, request.message, request.imageUriString)
        } catch (exception: Exception) {
            throw exception
        }
    }
}

data class CreatePostRequest(
    val message: String,
    val imageUriString: String
)