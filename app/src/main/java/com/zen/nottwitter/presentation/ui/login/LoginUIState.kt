package com.zen.nottwitter.presentation.ui.login

data class LoginUIState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: String = "",
    val isLoginButtonEnabled: Boolean = false,
)