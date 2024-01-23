package com.zen.nottwitter.domain.usecase

import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetUserPostsUseCaseTest : BaseTest() {

    private lateinit var useCase: GetUserPostsUseCase

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var contentRepository: ContentRepository

    @Before
    override fun setUp() {
        super.setUp()
        useCase = GetUserPostsUseCase(userRepository, contentRepository)
    }

    @Test
    fun `GetUserPostsUseCaseTest positive flow will return list of Post`() = runTest {
        coEvery { userRepository.getUser() } returns stubTestUser
        coEvery { contentRepository.getUserPosts(stubTestUser) } returns stubTestPosts
        Assert.assertEquals(stubTestPosts, useCase.execute(GetUserPostsRequest(false)))
        coVerify {
            userRepository.getUser()
            contentRepository.getUserPosts(stubTestUser)
        }
    }

    @Test
    fun `GetUserPostsUseCaseTest negative flow will throw Exception`() {
        coEvery { userRepository.getUser() } returns stubTestUser
        coEvery { contentRepository.getUserPosts(stubTestUser) } throws Exception()
        Assert.assertThrows(Exception::class.java) {
            runTest { useCase.execute(GetUserPostsRequest(false)) }
        }
        coVerify {
            userRepository.getUser()
            contentRepository.getUserPosts(stubTestUser)
        }
    }
}