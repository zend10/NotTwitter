package com.zen.nottwitter.presentation.ui.login

import com.zen.nottwitter.presentation.ui.base.BaseInteractionListener

interface LoginInteractionListener : BaseInteractionListener {
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onPasswordTrailingIconClick()
    fun onRegisterClick()
    fun onLoginClick()
}