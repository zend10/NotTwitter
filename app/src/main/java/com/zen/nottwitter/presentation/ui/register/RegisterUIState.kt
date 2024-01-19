package com.zen.nottwitter.presentation.ui.register

data class RegisterUIState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: String = "",
    val isRegisterButtonEnabled: Boolean = false,
)