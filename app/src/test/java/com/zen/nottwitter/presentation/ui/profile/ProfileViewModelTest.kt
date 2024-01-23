package com.zen.nottwitter.presentation.ui.profile

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.domain.usecase.GetLocalUserPostsUseCase
import com.zen.nottwitter.domain.usecase.GetUserPostsRequest
import com.zen.nottwitter.domain.usecase.GetUserPostsUseCase
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

class ProfileViewModelTest : BaseTest() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var state: StateFlow<ProfileUIState>

    @MockK
    private lateinit var contentRepository: ContentRepository

    @MockK
    private lateinit var configRepository: ConfigRepository

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var getUserPostsUseCase: GetUserPostsUseCase

    @MockK
    private lateinit var getLocalUserPostsUseCase: GetLocalUserPostsUseCase

    private val stubTestUserFlow = MutableSharedFlow<User>()
    private val stubTestNewPostFlow = MutableSharedFlow<Post>()

    @Before
    override fun setUp() {
        super.setUp()
        every { userRepository.user } returns stubTestUserFlow
        every { contentRepository.newPost } returns stubTestNewPostFlow
        viewModel = ProfileViewModel(
            userRepository,
            contentRepository,
            configRepository,
            getUserPostsUseCase,
            getLocalUserPostsUseCase,
            testDispatchers
        )
        state = viewModel.state
    }

    @Test
    fun `ProfileViewModel on init will listen to user Flow`() = runTest {
        val stubTestLocalPosts = listOf(Post("local"))
        coEvery { getLocalUserPostsUseCase.execute(Unit) } coAnswers {
            delay(5000)
            stubTestLocalPosts
        }
        coEvery { getUserPostsUseCase.execute(GetUserPostsRequest(false)) } coAnswers {
            delay(10000)
            stubTestPosts
        }

        stubTestUserFlow.emit(stubTestUser)
        assertEquals(true, state.value.isLoading)
        assertEquals(stubTestNickname, state.value.nickname)

        // local posts returns value
        advanceTimeBy(6000)
        assertEquals(stubTestLocalPosts, state.value.posts)
        assertEquals(true, state.value.isLoading)

        // posts returns value
        advanceTimeBy(6000)
        assertEquals(stubTestPosts, state.value.posts)
        assertEquals(false, state.value.isLoading)

        coVerifyOrder {
            getLocalUserPostsUseCase.execute(Unit)
            getUserPostsUseCase.execute(GetUserPostsRequest(false))
        }
    }

    @Test
    fun `HomeViewModel on init will listen to newPost Flow`() = runTest {
        coEvery { getLocalUserPostsUseCase.execute(Unit) } returns stubTestPosts

        stubTestUserFlow.emit(stubTestUser)
        assertEquals(stubTestPosts, state.value.posts)

        stubTestNewPostFlow.emit(stubTestPost)
        assertEquals(stubTestPost, state.value.posts.first())
    }

    @Test
    fun `onLogoutClick will update showLogoutDialog state`() {
        viewModel.onLogoutClick()
        assertEquals(true, state.value.showLogoutDialog)
    }

    @Test
    fun `onPostImageClick will emit ViewImage effect`() = runTest {
        viewModel.effect.test {
            viewModel.onPostImageClick(stubTestImageUrl)
            val latestEffect = awaitItem()
            assertTrue(latestEffect is ProfileUIEffect.ViewImage && latestEffect.imageUrl == stubTestImageUrl)
        }
    }

    @Test
    fun `onRefresh will trigger getUserPostsUserCase and update posts state`() = runTest {
        coEvery { getUserPostsUseCase.execute(GetUserPostsRequest(false)) } coAnswers {
            delay(5000)
            stubTestPosts
        }
        viewModel.onRefresh()
        assertEquals(true, state.value.isLoading)
        advanceTimeBy(6000)
        assertEquals(stubTestPosts, state.value.posts)
        assertEquals(false, state.value.isLoading)
        coVerify { getUserPostsUseCase.execute(GetUserPostsRequest(false)) }
    }

    @Test
    fun `onLoadNextPage when current state has more posts than pagination limit will trigger getUserPostsUserCase and update posts state`() =
        runTest {
            every { configRepository.getPaginationPerPageLimit() } returns 2
            coEvery { getLocalUserPostsUseCase.execute(Unit) } returns stubTestPosts
            coEvery { getUserPostsUseCase.execute(GetUserPostsRequest(true)) } coAnswers {
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

            coVerify(exactly = 2) { getUserPostsUseCase.execute(GetUserPostsRequest(true)) }
        }

    @Test
    fun `onLoadNextPage when current state has fewer posts than pagination limit will not trigger getUserPostsUserCase`() =
        runTest {
            every { configRepository.getPaginationPerPageLimit() } returns 2

            viewModel.onLoadNextPage()
            assertEquals(0, state.value.posts.size)

            coVerify(exactly = 0) { getUserPostsUseCase.execute(GetUserPostsRequest(true)) }
        }

    @Test
    fun `onLoadNextPage when current state isLoadingNextPage is still true will not trigger getUserPostsUserCase anymore`() =
        runTest {
            every { configRepository.getPaginationPerPageLimit() } returns 2
            coEvery { getLocalUserPostsUseCase.execute(Unit) } returns stubTestPosts
            coEvery { getUserPostsUseCase.execute(GetUserPostsRequest(true)) } coAnswers {
                delay(5000)
                stubTestPosts
            }

            stubTestUserFlow.emit(stubTestUser)

            viewModel.onLoadNextPage()
            viewModel.onLoadNextPage()
            viewModel.onLoadNextPage()

            coVerify(exactly = 1) { getUserPostsUseCase.execute(GetUserPostsRequest(true)) }
        }

    @Test
    fun `onLogoutDialogDismiss will update showLogoutDialog state`() {
        viewModel.onLogoutDialogDismiss()
        assertEquals(false, state.value.showLogoutDialog)
    }

    @Test
    fun `onLogoutDialogPositiveCtaClick will trigger logout`() {
        viewModel.onLogoutDialogPositiveCtaClick()
        coVerify { userRepository.logout() }
    }
}