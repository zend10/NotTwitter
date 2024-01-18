package com.zen.nottwitter.presentation.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

class LoginScreen : BaseScreen<LoginViewModel, LoginUIState, LoginUIEffect, LoginInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: LoginUIEffect, navigator: Navigator) {
        when (effect) {
            is LoginUIEffect.LoginFailed -> TODO()
            LoginUIEffect.LoginSuccess -> TODO()
            LoginUIEffect.NavigateToRegister -> TODO()
        }
    }

    @Composable
    override fun OnRender(state: LoginUIState, listener: LoginInteractionListener) {
        Column {
            Text(text = "Login ")
        }
    }
}