package com.zen.nottwitter.presentation.ui.register

import com.zen.nottwitter.presentation.ui.base.BaseInteractionListener

interface RegisterInteractionListener : BaseInteractionListener {
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onPasswordTrailingIconClick()
    fun onRegisterClick()
}