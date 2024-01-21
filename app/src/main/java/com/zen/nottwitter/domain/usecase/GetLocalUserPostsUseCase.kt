package com.zen.nottwitter.domain.usecase

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository

class GetLocalUserPostsUseCase(
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository
) : UseCase<Unit, List<Post>> {

    override suspend fun execute(request: Unit): List<Post> {
        try {
            val user = userRepository.getUser()
            return contentRepository.getLocalUserPosts(user)
        } catch (exception: Exception) {
            throw exception
        }
    }
}