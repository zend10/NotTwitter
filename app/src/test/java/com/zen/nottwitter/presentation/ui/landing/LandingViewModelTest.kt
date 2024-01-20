package com.zen.nottwitter.presentation.ui.landing

import app.cash.turbine.test
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.presentation.ui.base.MainCoroutineRule
import com.zen.nottwitter.presentation.ui.base.TestDispatchers
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LandingViewModelTest {

    private val testDispatchers: TestDispatchers = TestDispatchers()
    private lateinit var viewModel: LandingViewModel

    @MockK
    private lateinit var userRepository: UserRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(testDispatchers.testDispatcher)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `LandingViewModel on init will do authenticate and emit AuthenticationSuccess effect if User is non null`() {
        val stubTestUser = User("", "", "")
        coEvery { userRepository.authenticate() } returns stubTestUser
        viewModel = LandingViewModel(userRepository, testDispatchers)
        runTest {
            viewModel.effect.test {
                advanceUntilIdle()
                coVerify { userRepository.authenticate() }
                assertEquals(LandingUIEffect.AuthenticationSuccess, awaitItem())
            }
        }
    }

    @Test
    fun `LandingViewModel on init will do authenticate and emit FirstTimeUser effect if User is null`() {
        coEvery { userRepository.authenticate() } returns null
        viewModel = LandingViewModel(userRepository, testDispatchers)
        runTest {
            viewModel.effect.test {
                advanceUntilIdle()
                coVerify { userRepository.authenticate() }
                assertEquals(LandingUIEffect.FirstTimeUser, awaitItem())
            }
        }
    }

    @Test
    fun `LandingViewModel on init will do authenticate and emit AuthenticationFailed effect if is there is an exception`() {
        coEvery { userRepository.authenticate() } throws Exception()
        viewModel = LandingViewModel(userRepository, testDispatchers)
        runTest {
            viewModel.effect.test {
                advanceUntilIdle()
                coVerify {
                    userRepository.authenticate()
                    userRepository.logout()
                }
                assertEquals(LandingUIEffect.AuthenticationFailed, awaitItem())
            }
        }
    }
}