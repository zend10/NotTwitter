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

class GetLocalUserPostsUseCaseTest : BaseTest() {

    private lateinit var useCase: GetLocalUserPostsUseCase

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var contentRepository: ContentRepository

    @Before
    override fun setUp() {
        super.setUp()
        useCase = GetLocalUserPostsUseCase(userRepository, contentRepository)
    }

    @Test
    fun `GetLocalUserPostsUseCaseTest positive flow will return list of Post`() = runTest {
        coEvery { userRepository.getUser() } returns stubTestUser
        coEvery { contentRepository.getLocalUserPosts(stubTestUser) } returns stubTestPosts
        assertEquals(stubTestPosts, useCase.execute(Unit))
        coVerify {
            userRepository.getUser()
            contentRepository.getLocalUserPosts(stubTestUser)
        }
    }

    @Test
    fun `GetLocalUserPostsUseCaseTest negative flow will throw Exception`() {
        coEvery { userRepository.getUser() } returns stubTestUser
        coEvery { contentRepository.getLocalUserPosts(stubTestUser) } throws Exception()
        assertThrows(Exception::class.java) {
            runTest { useCase.execute(Unit) }
        }
        coVerify {
            userRepository.getUser()
            contentRepository.getLocalUserPosts(stubTestUser)
        }
    }
}