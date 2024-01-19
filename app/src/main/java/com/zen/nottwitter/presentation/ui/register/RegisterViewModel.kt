package com.zen.nottwitter.presentation.ui.register

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.domain.isValidEmail
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) :
    BaseViewModel<RegisterUIState, RegisterUIEffect>(RegisterUIState()),
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
        screenModelScope.launch(Dispatchers.IO) {
            try {
                updateState { it.copy(isLoading = true) }
                userRepository.register(
                    state.value.nickname,
                    state.value.email,
                    state.value.password
                )
                sendNewEffect(RegisterUIEffect.RegisterSuccess)
            } catch (exception: Exception) {
                updateState {
                    it.copy(
                        errorMessage = exception.localizedMessage ?: "Something went wrong."
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
        val isNicknameValid = nickname.trim().length in 3..32
        val isEmailValid = email.trim().isValidEmail()
        val isPasswordValid = password.length in 6..32
        return isNicknameValid && isEmailValid && isPasswordValid
    }
}