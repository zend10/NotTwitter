package com.zen.nottwitter.presentation.ui.login

sealed interface LoginUIEffect {
    object NavigateToRegister : LoginUIEffect
    object LoginSuccess : LoginUIEffect
    class LoginFailed(val errorMessage: String) : LoginUIEffect
}