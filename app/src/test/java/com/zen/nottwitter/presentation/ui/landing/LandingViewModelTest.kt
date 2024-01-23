package com.zen.nottwitter.presentation.ui.landing

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LandingViewModelTest : BaseTest() {

    private lateinit var viewModel: LandingViewModel

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `LandingViewModel on init will do authenticate and emit AuthenticationSuccess effect if User is non null`() {
        val stubTestUser = User()
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