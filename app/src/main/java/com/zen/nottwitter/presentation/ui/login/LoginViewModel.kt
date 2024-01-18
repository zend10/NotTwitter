package com.zen.nottwitter.presentation.ui.login

import com.zen.nottwitter.presentation.ui.base.BaseViewModel

class LoginViewModel : BaseViewModel<LoginUIState, LoginUIEffect>(LoginUIState()), LoginInteractionListener {

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
        updateState {
            it.copy(errorMessage = "Oops")
        }
    }

    private fun isLoginButtonEnabled(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }
}