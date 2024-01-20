package com.zen.nottwitter.presentation.ui.login

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.domain.isValidEmail
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository, dispatchers: DispatcherProvider) :
    BaseViewModel<LoginUIState, LoginUIEffect>(LoginUIState(), dispatchers),
    LoginInteractionListener {

    override fun onEmailChange(email: String) {
        updateState {
            it.copy(
                email = email,
                isLoginButtonEnabled = isLoginButtonEnabled(email, it.password)
            )
        }
    }

    override fun onPasswordChange(password: String) {
        updateState {
            it.copy(
                password = password,
                isLoginButtonEnabled = isLoginButtonEnabled(it.email, password)
            )
        }
    }

    override fun onPasswordTrailingIconClick() {
        updateState {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    override fun onRegisterClick() {
        sendNewEffect(LoginUIEffect.NavigateToRegister)
    }

    override fun onLoginClick() {
        screenModelScope.launch(dispatchers.io) {
            try {
                updateState { it.copy(isLoading = true) }
                userRepository.login(state.value.email, state.value.password)
                sendNewEffect(LoginUIEffect.LoginSuccess)
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

    private fun isLoginButtonEnabled(email: String, password: String): Boolean {
        val isEmailValid = email.trim().isValidEmail()
        val isPasswordValid = password.length in 6..32
        return isEmailValid && isPasswordValid
    }
}