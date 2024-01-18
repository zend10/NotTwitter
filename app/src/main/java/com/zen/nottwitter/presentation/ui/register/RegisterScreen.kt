package com.zen.nottwitter.presentation.ui.register

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

class RegisterScreen :
    BaseScreen<RegisterViewModel, RegisterUIState, RegisterUIEffect, RegisterInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: RegisterUIEffect, navigator: Navigator) {
        when (effect) {
            RegisterUIEffect.RegisterSuccess -> {}
        }
    }

    @Composable
    override fun OnRender(state: RegisterUIState, listener: RegisterInteractionListener) {
        Text(text = "Register")
    }
}