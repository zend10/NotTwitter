package com.zen.nottwitter.presentation.ui.landing

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.AppIcon
import com.zen.nottwitter.presentation.ui.login.LoginScreen

class LandingScreen :
    BaseScreen<LandingViewModel, LandingUIState, LandingUIEffect, LandingInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: LandingUIEffect, navigator: Navigator) {
        when (effect) {
            is LandingUIEffect.FirstTimeUser -> {
                navigator.replaceAll(LoginScreen())
            }
            is LandingUIEffect.UserVerificationFailed -> {

            }
            LandingUIEffect.UserVerificationSuccess -> {

            }
        }
    }

    @Composable
    override fun OnRender(state: LandingUIState, listener: LandingInteractionListener) {
        if (state.isLoading) {
            AppIcon()
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun LandingScreenPreview() {
        OnRender(state = LandingUIState(), listener = object : LandingInteractionListener {})
    }
}