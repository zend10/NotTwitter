package com.zen.nottwitter.presentation.ui.login

import app.cash.turbine.test
import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModelTest
import com.zen.nottwitter.presentation.ui.base.MainCoroutineRule
import com.zen.nottwitter.presentation.ui.base.TestDispatchers
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : BaseViewModelTest() {

    private val testDispatchers: TestDispatchers = TestDispatchers()
    private lateinit var viewModel: LoginViewModel
    private lateinit var state: StateFlow<LoginUIState>

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var configRepository: ConfigRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(testDispatchers.testDispatcher)

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
        coEvery { userRepository.login(stubTestEmail, stubTestPassword) } returns stubTestUser

        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)

        runTest {
            viewModel.effect.test {
                viewModel.onLoginClick()
                coVerify { userRepository.login(stubTestEmail, stubTestPassword) }
                assertEquals(LoginUIEffect.LoginSuccess, awaitItem())
                assertEquals(false, state.value.isLoading)
            }
        }
    }

    @Test
    fun `onLoginClick negative flow will update errorMessage state`() {
        val stubTestException = spyk<Exception>()
        val stubTestErrorMessage = "Oops"
        coEvery { userRepository.login(stubTestEmail, stubTestPassword) } throws stubTestException
        every { stubTestException.localizedMessage } returns stubTestErrorMessage andThen null

        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)

        runTest {
            viewModel.effect.test {
                viewModel.onLoginClick()
                assertEquals(stubTestErrorMessage, state.value.errorMessage)

                viewModel.onLoginClick()
                assertEquals(stubTestFallbackErrorMessage, state.value.errorMessage)

                coVerify(exactly = 2) { userRepository.login(stubTestEmail, stubTestPassword) }
                expectNoEvents()
                assertEquals(false, state.value.isLoading)
            }
        }
    }
}