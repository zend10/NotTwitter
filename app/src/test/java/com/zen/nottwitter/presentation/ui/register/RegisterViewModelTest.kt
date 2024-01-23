package com.zen.nottwitter.presentation.ui.register

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

class RegisterViewModelTest : BaseTest() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var state: StateFlow<RegisterUIState>

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var configRepository: ConfigRepository

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = RegisterViewModel(userRepository, configRepository, testDispatchers)
        state = viewModel.state

        every { configRepository.getUserConfig() } returns stubTestUserConfig
        every { configRepository.getFallbackErrorMessage() } returns stubTestFallbackErrorMessage
    }

    @Test
    fun `onNicknameChange will update email state`() {
        viewModel.onNicknameChange(stubTestNickname)
        assertEquals(stubTestNickname, state.value.nickname)
        assertEquals(false, state.value.isRegisterButtonEnabled)
    }

    @Test
    fun `onEmailChange will update email state`() {
        viewModel.onEmailChange(stubTestEmail)
        assertEquals(stubTestEmail, state.value.email)
        assertEquals(false, state.value.isRegisterButtonEnabled)
    }

    @Test
    fun `onPasswordChange will update password state`() {
        viewModel.onPasswordChange(stubTestPassword)
        assertEquals(stubTestPassword, state.value.password)
        assertEquals(false, state.value.isRegisterButtonEnabled)
    }

    @Test
    fun `isRegisterButtonEnabled will be true if all of nickname email and password are valid`() {
        // all valid
        viewModel.onNicknameChange(stubTestNickname)
        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)
        assertEquals(true, state.value.isRegisterButtonEnabled)

        // invalid nickname fewer than 3 characters
        viewModel.onNicknameChange("A")
        assertEquals(false, state.value.isRegisterButtonEnabled)

        // invalid nickname more than 32 characters
        viewModel.onNicknameChange("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz")
        assertEquals(false, state.value.isRegisterButtonEnabled)

        // invalid email
        viewModel.onNicknameChange(stubTestNickname)
        viewModel.onEmailChange("random_text")
        assertEquals(false, state.value.isRegisterButtonEnabled)

        // invalid password fewer than 6 characters
        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange("abc")
        assertEquals(false, state.value.isRegisterButtonEnabled)

        // invalid password more than 32 characters
        viewModel.onPasswordChange("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz")
        assertEquals(false, state.value.isRegisterButtonEnabled)
    }

    @Test
    fun `onPasswordTrailingIconClick will reverse isPasswordVisible state`() {
        viewModel.onPasswordTrailingIconClick()
        assertEquals(true, state.value.isPasswordVisible)
        viewModel.onPasswordTrailingIconClick()
        assertEquals(false, state.value.isPasswordVisible)
    }

    @Test
    fun `onBackButtonClick will emit NavigateBack effect`() {
        runTest {
            viewModel.effect.test {
                viewModel.onBackButtonClick()
                assertEquals(RegisterUIEffect.NavigateBack, awaitItem())
            }
        }
    }

    @Test
    fun `onRegisterClick positive flow will emit RegisterSuccess effect`() {
        coEvery {
            userRepository.register(
                stubTestNickname,
                stubTestEmail,
                stubTestPassword
            )
        } coAnswers {
            delay(5000)
            stubTestUser
        }

        viewModel.onNicknameChange(stubTestNickname)
        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)

        runTest {
            viewModel.effect.test {
                viewModel.onRegisterClick()
                assertEquals(true, state.value.isLoading)
                advanceTimeBy(6000)
                assertEquals(RegisterUIEffect.RegisterSuccess, awaitItem())
                assertEquals(false, state.value.isLoading)
                coVerify {
                    userRepository.register(
                        stubTestNickname,
                        stubTestEmail,
                        stubTestPassword
                    )
                }
            }
        }
    }

    @Test
    fun `onRegisterClick negative flow will update errorMessage state`() {
        val stubTestException = spyk<Exception>()
        val stubTestErrorMessage = "Oops"
        coEvery {
            userRepository.register(
                stubTestNickname,
                stubTestEmail,
                stubTestPassword
            )
        } coAnswers {
            delay(5000)
            throw stubTestException
        }
        every { stubTestException.localizedMessage } returns stubTestErrorMessage andThen null

        viewModel.onNicknameChange(stubTestNickname)
        viewModel.onEmailChange(stubTestEmail)
        viewModel.onPasswordChange(stubTestPassword)

        runTest {
            viewModel.effect.test {
                viewModel.onRegisterClick()
                assertEquals(true, state.value.isLoading)
                advanceTimeBy(6000)
                assertEquals(stubTestErrorMessage, state.value.errorMessage)
                assertEquals(false, state.value.isLoading)

                viewModel.onRegisterClick()
                assertEquals(true, state.value.isLoading)
                advanceTimeBy(6000)
                assertEquals(stubTestFallbackErrorMessage, state.value.errorMessage)
                assertEquals(false, state.value.isLoading)

                expectNoEvents()
                coVerify(exactly = 2) {
                    userRepository.register(
                        stubTestNickname,
                        stubTestEmail,
                        stubTestPassword
                    )
                }
            }
        }
    }
}