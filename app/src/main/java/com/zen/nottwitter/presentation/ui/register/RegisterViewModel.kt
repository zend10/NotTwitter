package com.zen.nottwitter.presentation.ui.register

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.domain.isValidEmail
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val configRepository: ConfigRepository,
    dispatchers: DispatcherProvider
) :
    BaseViewModel<RegisterUIState, RegisterUIEffect>(RegisterUIState(), dispatchers),
    RegisterInteractionListener {

    override fun onNicknameChange(nickname: String) {
        updateState {
            it.copy(
                nickname = nickname,
                isRegisterButtonEnabled = isRegisterButtonEnabled(nickname, it.email, it.password)
            )
        }
    }

    override fun onEmailChange(email: String) {
        updateState {
            it.copy(
                email = email,
                isRegisterButtonEnabled = isRegisterButtonEnabled(it.nickname, email, it.password)
            )
        }
    }

    override fun onPasswordChange(password: String) {
        updateState {
            it.copy(
                password = password,
                isRegisterButtonEnabled = isRegisterButtonEnabled(it.nickname, it.email, password)
            )
        }
    }

    override fun onPasswordTrailingIconClick() {
        updateState {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    override fun onRegisterClick() {
        screenModelScope.launch(dispatchers.io) {
            try {
                updateState { it.copy(isLoading = true) }
                userRepository.register(
                    state.value.nickname,
                    state.value.email,
                    state.value.password
                )
                sendNewEffect(RegisterUIEffect.RegisterSuccess)
            } catch (exception: Exception) {
                val fallbackErrorMessage = configRepository.getFallbackErrorMessage()
                updateState {
                    it.copy(
                        errorMessage = exception.localizedMessage ?: fallbackErrorMessage
                    )
                }
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    override fun onBackButtonPressed() {
        sendNewEffect(RegisterUIEffect.NavigateBack)
    }

    private fun isRegisterButtonEnabled(
        nickname: String,
        email: String,
        password: String
    ): Boolean {
        val userConfig = configRepository.getUserConfig()
        val isNicknameValid =
            nickname.trim().length in userConfig.nicknameMinLength..userConfig.nicknameMaxLength
        val isEmailValid = email.trim().isValidEmail()
        val isPasswordValid =
            password.length in userConfig.passwordMinLength..userConfig.passwordMaxLength
        return isNicknameValid && isEmailValid && isPasswordValid
    }
}