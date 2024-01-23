package com.zen.nottwitter.domain.usecase

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository

class GetUserPostsUseCase(
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository
) : UseCase<GetUserPostsRequest, List<Post>> {

    override suspend fun execute(request: GetUserPostsRequest): List<Post> {
        try {
            val user = userRepository.getUser()
            return contentRepository.getUserPosts(user, request.loadNextPage)
        } catch (exception: Exception) {
            throw exception
        }
    }
}

data class GetUserPostsRequest(
    val loadNextPage: Boolean
)