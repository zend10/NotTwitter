package com.zen.nottwitter.presentation.ui.landing

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

class LandingScreen : BaseScreen<LandingViewModel, LandingUIState, LandingUIEffect, LandingInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: LandingUIEffect, navigator: Navigator) {
        when (effect) {
            is LandingUIEffect.UserVerificationFailed -> TODO()
            LandingUIEffect.UserVerificationSuccess -> TODO()
        }
    }

    @Composable
    override fun OnRender(state: LandingUIState, listener: LandingInteractionListener) {
        Text(text = "Landing")
    }
}