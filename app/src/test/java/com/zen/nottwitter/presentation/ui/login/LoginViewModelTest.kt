package com.zen.nottwitter.presentation.ui.login

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginViewModelTest : BaseTest() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var state: StateFlow<LoginUIState>

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var configRepository: ConfigRepository

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = LoginViewModel(userRepository, configRepository, testDispatchers)
        state = viewModel.state

        every { configRepository.getUserConfig() } returns stubTestUserConfig
        every { configRepository.getFallbackErrorMessage() } returns stubTestFallbackErrorMessage
    }

    @Test
    fun `onEmailChange will update email state`() {
        viewModel.onEmailChange(stubTestEmail)
        assertEquals(stubTestEmail, state.value.email)
        assertEquals(false, state.value.isLoginButtonEnabled)
    }

    @Test
    fun `onPasswordChange will update password state`() {
        viewModel.onPasswordChange(stubTestPassword)
        assertEquals(stubTestPassword, state.value.password)
        assertEquals(false, state.value.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled will be true if both email and password are valid`() {
        // both valid
        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)
        assertEquals(true, state.value.isLoginButtonEnabled)

        // invalid email
        viewModel.onEmailChange("random_text")
        assertEquals(false, state.value.isLoginButtonEnabled)

        // invalid password fewer than 6 characters
        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange("abc")
        assertEquals(false, state.value.isLoginButtonEnabled)

        // invalid password more than 32 characters
        viewModel.onPasswordChange("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz")
        assertEquals(false, state.value.isLoginButtonEnabled)
    }

    @Test
    fun `onPasswordTrailingIconClick will reverse isPasswordVisible state`() {
        viewModel.onPasswordTrailingIconClick()
        assertEquals(true, state.value.isPasswordVisible)
        viewModel.onPasswordTrailingIconClick()
        assertEquals(false, state.value.isPasswordVisible)
    }

    @Test
    fun `onRegisterClick will emit NavigateToRegister effect`() {
        runTest {
            viewModel.effect.test {
                viewModel.onRegisterClick()
                assertEquals(LoginUIEffect.NavigateToRegister, awaitItem())
            }
        }
    }

    @Test
    fun `onLoginClick positive flow will emit LoginSuccess effect`() {
        coEvery { userRepository.login(stubTestEmail, stubTestPassword) } coAnswers {
            delay(5000)
            stubTestUser
        }

        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)

        runTest {
            viewModel.effect.test {
                viewModel.onLoginClick()
                assertEquals(true, state.value.isLoading)
                advanceTimeBy(6000)
                assertEquals(LoginUIEffect.LoginSuccess, awaitItem())
                assertEquals(false, state.value.isLoading)
                coVerify { userRepository.login(stubTestEmail, stubTestPassword) }
            }
        }
    }

    @Test
    fun `onLoginClick negative flow will update errorMessage state`() {
        val stubTestException = spyk<Exception>()
        val stubTestErrorMessage = "Oops"
        coEvery { userRepository.login(stubTestEmail, stubTestPassword) } coAnswers {
            delay(5000)
            throw stubTestException
        }
        every { stubTestException.localizedMessage } returns stubTestErrorMessage andThen null

        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)

        runTest {
            viewModel.effect.test {
                viewModel.onLoginClick()
                assertEquals(true, state.value.isLoading)
                advanceTimeBy(6000)
                assertEquals(stubTestErrorMessage, state.value.errorMessage)
                assertEquals(false, state.value.isLoading)

                viewModel.onLoginClick()
                advanceTimeBy(6000)
                assertEquals(stubTestFallbackErrorMessage, state.value.errorMessage)
                assertEquals(false, state.value.isLoading)

                expectNoEvents()
                coVerify(exactly = 2) { userRepository.login(stubTestEmail, stubTestPassword) }
            }
        }
    }
}