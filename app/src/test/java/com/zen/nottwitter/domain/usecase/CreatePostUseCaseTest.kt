package com.zen.nottwitter.domain.usecase

import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class CreatePostUseCaseTest : BaseTest() {

    private lateinit var useCase: CreatePostUseCase

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var contentRepository: ContentRepository

    @Before
    override fun setUp() {
        super.setUp()
        useCase = CreatePostUseCase(userRepository, contentRepository)
    }

    @Test
    fun `CreatePostUseCase positive flow will return Post`() = runTest {
        coEvery { userRepository.getUser() } returns stubTestUser
        coEvery {
            contentRepository.createPost(
                stubTestUser,
                stubTestMessage,
                stubTestImageUrl
            )
        } returns stubTestPost
        assertEquals(
            stubTestPost,
            useCase.execute(CreatePostRequest(stubTestMessage, stubTestImageUrl))
        )
        coVerify {
            userRepository.getUser()
            contentRepository.createPost(stubTestUser, stubTestMessage, stubTestImageUrl)
        }
    }

    @Test
    fun `CreatePostUseCase negative flow will throw Exception`() {
        coEvery { userRepository.getUser() } returns stubTestUser
        coEvery {
            contentRepository.createPost(
                stubTestUser,
                stubTestMessage,
                stubTestImageUrl
            )
        } throws Exception()
        assertThrows(Exception::class.java) {
            runTest { useCase.execute(CreatePostRequest(stubTestMessage, stubTestImageUrl)) }
        }
        coVerify {
            userRepository.getUser()
            contentRepository.createPost(stubTestUser, stubTestMessage, stubTestImageUrl)
        }
    }
}