package com.zen.nottwitter.presentation.ui.main

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest : BaseTest() {

    private lateinit var viewModel: MainViewModel
    private lateinit var state: StateFlow<MainUIState>

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var contentRepository: ContentRepository

    private val stubTestSendingPostFlow = MutableSharedFlow<Boolean>()
    private val stubTestLogoutTriggerFlow = MutableSharedFlow<Unit>()

    @Before
    override fun setUp() {
        super.setUp()
        every { contentRepository.sendingPost } returns stubTestSendingPostFlow
        every { userRepository.logoutTrigger } returns stubTestLogoutTriggerFlow
        viewModel = MainViewModel(userRepository, contentRepository, testDispatchers)
    }

    @Test
    fun `MainViewModel on init will listen to sendingPost Flow`() = runTest {
        stubTestSendingPostFlow.emit(true)
        assertEquals(true, viewModel.state.value.isLoading)
        stubTestSendingPostFlow.emit(false)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `MainViewModel on init will listen to logoutTrigger Flow`() = runTest {
        viewModel.effect.test {
            stubTestLogoutTriggerFlow.emit(Unit)
            assertEquals(MainUIEffect.Logout, awaitItem())
        }
    }
}