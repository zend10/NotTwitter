package com.zen.nottwitter.data.repository

import app.cash.turbine.turbineScope
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.network.FirebaseProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class DefaultContentRepositoryTest : BaseTest() {

    private lateinit var repository: DefaultContentRepository

    @MockK
    private lateinit var firebaseProvider: FirebaseProvider

    @MockK
    private lateinit var localStorageProvider: LocalStorageProvider

    @Before
    override fun setUp() {
        super.setUp()
        repository = DefaultContentRepository(firebaseProvider, localStorageProvider)
    }

    @Test
    fun `createPost positive flow will emit sendingPost and newPost and return Post`() = runTest {
        turbineScope {
            coEvery {
                firebaseProvider.createPost(
                    stubTestUser,
                    stubTestMessage,
                    stubTestImageUrl
                )
            } returns stubTestPost
            val sendingPostTurbine = repository.sendingPost.testIn(backgroundScope)
            val newPostTurbine = repository.newPost.testIn(backgroundScope)
            assertEquals(
                stubTestPost,
                repository.createPost(stubTestUser, stubTestMessage, stubTestImageUrl)
            )
            assertEquals(false, sendingPostTurbine.awaitItem())
            assertEquals(true, sendingPostTurbine.awaitItem())
            assertEquals(false, sendingPostTurbine.awaitItem())
            assertEquals(stubTestPost, newPostTurbine.awaitItem())
        }
    }

    @Test
    fun `createPost will throw an exception if get an exception`() = runTest {
        turbineScope {
            coEvery {
                firebaseProvider.createPost(
                    stubTestUser,
                    stubTestMessage,
                    stubTestImageUrl
                )
            } throws Exception()
            val sendingPostTurbine = repository.sendingPost.testIn(backgroundScope)
            val newPostTurbine = repository.newPost.testIn(backgroundScope)
            assertThrows(Exception::class.java) {
                kotlinx.coroutines.test.runTest {
                    repository.createPost(
                        stubTestUser,
                        stubTestMessage,
                        stubTestImageUrl
                    )
                }
            }
            assertEquals(false, sendingPostTurbine.awaitItem())
            assertEquals(true, sendingPostTurbine.awaitItem())
            assertEquals(false, sendingPostTurbine.awaitItem())
            newPostTurbine.expectNoEvents()
        }
    }

    @Test
    fun `getPosts positive flow with loadNextPage false will save posts to local storage and return posts`() =
        runTest {
            coEvery { firebaseProvider.getPosts(false) } returns stubTestPosts
            coEvery { localStorageProvider.savePosts(stubTestPosts) } returns stubTestPosts
            assertEquals(stubTestPosts, repository.getPosts(false))
            coVerify {
                firebaseProvider.getPosts(false)
                localStorageProvider.deletePosts()
                localStorageProvider.savePosts(stubTestPosts)
            }
        }

    @Test
    fun `getPosts positive flow with loadNextPage true will not save posts to local storage and return posts`() =
        runTest {
            coEvery { firebaseProvider.getPosts(true) } returns stubTestPosts
            assertEquals(stubTestPosts, repository.getPosts(true))
            coVerify {
                firebaseProvider.getPosts(true)
            }
            coVerify(exactly = 0) {
                localStorageProvider.deletePosts()
                localStorageProvider.savePosts(stubTestPosts)
            }
        }

    @Test
    fun `getPosts will throw an exception if get an exception`() = runTest {
        coEvery { firebaseProvider.getPosts(true) } throws Exception()
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.test.runTest { repository.getPosts(true) }
        }
        coVerify {
            firebaseProvider.getPosts(true)
        }
        coVerify(exactly = 0) {
            localStorageProvider.deletePosts()
            localStorageProvider.savePosts(stubTestPosts)
        }
    }

    @Test
    fun `getLocalPosts will return posts`() = runTest {
        coEvery { localStorageProvider.getPosts() } returns stubTestPosts
        assertEquals(stubTestPosts, repository.getLocalPosts())
    }

    @Test
    fun `getUserPosts positive flow with loadNextPage false will save user posts to local storage and return user posts`() =
        runTest {
            coEvery { firebaseProvider.getUserPosts(stubTestUser, false) } returns stubTestPosts
            coEvery { localStorageProvider.saveUserPosts(stubTestPosts) } returns stubTestPosts
            assertEquals(stubTestPosts, repository.getUserPosts(stubTestUser, false))
            coVerify {
                firebaseProvider.getUserPosts(stubTestUser, false)
                localStorageProvider.deleteUserPosts()
                localStorageProvider.saveUserPosts(stubTestPosts)
            }
        }

    @Test
    fun `getUserPosts positive flow with loadNextPage true will not save user posts to local storage and return user posts`() =
        runTest {
            coEvery { firebaseProvider.getUserPosts(stubTestUser, true) } returns stubTestPosts
            assertEquals(stubTestPosts, repository.getUserPosts(stubTestUser, true))
            coVerify {
                firebaseProvider.getUserPosts(stubTestUser, true)
            }
            coVerify(exactly = 0) {
                localStorageProvider.deleteUserPosts()
                localStorageProvider.saveUserPosts(stubTestPosts)
            }
        }

    @Test
    fun `getUserPosts will throw an exception if get an exception`() = runTest {
        coEvery { firebaseProvider.getUserPosts(stubTestUser, true) } throws Exception()
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.test.runTest { repository.getUserPosts(stubTestUser, true) }
        }
        coVerify {
            firebaseProvider.getUserPosts(stubTestUser, true)
        }
        coVerify(exactly = 0) {
            localStorageProvider.deleteUserPosts()
            localStorageProvider.saveUserPosts(stubTestPosts)
        }
    }

    @Test
    fun `getLocalUserPosts will return posts`() = runTest {
        coEvery { localStorageProvider.getUserPosts() } returns stubTestPosts
        assertEquals(stubTestPosts, repository.getLocalUserPosts(stubTestUser))
    }
}