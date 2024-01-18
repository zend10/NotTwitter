package com.zen.nottwitter.presentation.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.base.BaseScreen
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
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(
                    id = R.string.app_name
                ),
            )
        }
    }
}