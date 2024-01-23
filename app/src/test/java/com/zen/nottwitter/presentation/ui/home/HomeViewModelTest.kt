package com.zen.nottwitter.presentation.ui.home

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HomeViewModelTest : BaseTest() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var state: StateFlow<HomeUIState>

    @MockK
    private lateinit var contentRepository: ContentRepository

    @MockK
    private lateinit var configRepository: ConfigRepository

    @MockK
    private lateinit var userRepository: UserRepository

    private val stubTestUserFlow = MutableSharedFlow<User>()
    private val stubTestNewPostFlow = MutableSharedFlow<Post>()

    @Before
    override fun setUp() {
        super.setUp()
        every { userRepository.user } returns stubTestUserFlow
        every { contentRepository.newPost } returns stubTestNewPostFlow
        viewModel =
            HomeViewModel(contentRepository, configRepository, userRepository, testDispatchers)
        state = viewModel.state
    }

    @Test
    fun `HomeViewModel on init will listen to user Flow`() = runTest {
        val stubTestLocalPosts = listOf(Post("local"))
        coEvery { contentRepository.getLocalPosts() } coAnswers {
            delay(5000)
            stubTestLocalPosts
        }
        coEvery { contentRepository.getPosts() } coAnswers {
            delay(10000)
            stubTestPosts
        }

        stubTestUserFlow.emit(stubTestUser)
        assertEquals(true, state.value.isLoading)

        // local posts returns value
        advanceTimeBy(6000)
        assertEquals(stubTestLocalPosts, state.value.posts)
        assertEquals(true, state.value.isLoading)

        // posts returns value
        advanceTimeBy(6000)
        assertEquals(stubTestPosts, state.value.posts)
        assertEquals(false, state.value.isLoading)

        coVerifyOrder {
            contentRepository.getLocalPosts()
            contentRepository.getPosts()
        }
    }

    @Test
    fun `HomeViewModel on init will listen to newPost Flow`() = runTest {
        coEvery { contentRepository.getLocalPosts() } returns stubTestPosts

        stubTestUserFlow.emit(stubTestUser)
        assertEquals(stubTestPosts, state.value.posts)

        stubTestNewPostFlow.emit(stubTestPost)
        assertEquals(stubTestPost, state.value.posts.first())
    }

    @Test
    fun `onNewPostClick will emit NavigateToPostEditor effect`() = runTest {
        viewModel.effect.test {
            viewModel.onNewPostClick()
            assertEquals(HomeUIEffect.NavigateToPostEditor, awaitItem())
        }
    }

    @Test
    fun `onPostImageClick will emit ViewImage effect`() = runTest {
        viewModel.effect.test {
            viewModel.onPostImageClick(stubTestImageUrl)
            val latestEffect = awaitItem()
            assertTrue(latestEffect is HomeUIEffect.ViewImage && latestEffect.imageUrl == stubTestImageUrl)
        }
    }

    @Test
    fun `onRefresh will trigger getPosts and update posts state`() = runTest {
        coEvery { contentRepository.getPosts() } coAnswers {
            delay(5000)
            stubTestPosts
        }
        viewModel.onRefresh()
        assertEquals(true, state.value.isLoading)
        advanceTimeBy(6000)
        assertEquals(stubTestPosts, state.value.posts)
        assertEquals(false, state.value.isLoading)
        coVerify { contentRepository.getPosts() }
    }

    @Test
    fun `onLoadNextPage when current state has more posts than pagination limit will trigger getPosts and update posts state`() =
        runTest {
            every { configRepository.getPaginationPerPageLimit() } returns 2
            coEvery { contentRepository.getLocalPosts() } returns stubTestPosts
            coEvery { contentRepository.getPosts(true) } coAnswers {
                delay(5000)
                stubTestPosts
            }

            stubTestUserFlow.emit(stubTestUser)
            assertEquals(stubTestPosts, state.value.posts)

            viewModel.onLoadNextPage()
            assertEquals(true, state.value.isLoadingNextPage)
            advanceTimeBy(6000)
            assertEquals(stubTestPosts + stubTestPosts, state.value.posts)
            assertEquals(false, state.value.isLoadingNextPage)

            viewModel.onLoadNextPage()
            assertEquals(true, state.value.isLoadingNextPage)
            advanceTimeBy(6000)
            assertEquals(stubTestPosts + stubTestPosts + stubTestPosts, state.value.posts)
            assertEquals(false, state.value.isLoadingNextPage)

            coVerify(exactly = 2) { contentRepository.getPosts(true) }
        }

    @Test
    fun `onLoadNextPage when current state has fewer posts than pagination limit will not trigger getPosts`() =
        runTest {
            every { configRepository.getPaginationPerPageLimit() } returns 2

            viewModel.onLoadNextPage()
            assertEquals(0, state.value.posts.size)

            coVerify(exactly = 0) { contentRepository.getPosts(true) }
        }

    @Test
    fun `onLoadNextPage when current state isLoadingNextPage is still true will not trigger getPosts anymore`() =
        runTest {
            every { configRepository.getPaginationPerPageLimit() } returns 2
            coEvery { contentRepository.getLocalPosts() } returns stubTestPosts
            coEvery { contentRepository.getPosts(true) } coAnswers {
                delay(5000)
                stubTestPosts
            }

            stubTestUserFlow.emit(stubTestUser)

            viewModel.onLoadNextPage()
            viewModel.onLoadNextPage()
            viewModel.onLoadNextPage()

            coVerify(exactly = 1) { contentRepository.getPosts(true) }
        }
}