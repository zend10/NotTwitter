package com.zen.nottwitter.presentation.ui.register

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.domain.isValidEmail
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel<RegisterUIState, RegisterUIEffect>(RegisterUIState()),
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
            updateState { it.copy(isLoading = true) }
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
        val isNicknameValid = nickname.length in 3..32
        val isEmailValid = email.isValidEmail()
        val isPasswordValid = password.length in 6..32
        return isNicknameValid && isEmailValid && isPasswordValid
    }
}