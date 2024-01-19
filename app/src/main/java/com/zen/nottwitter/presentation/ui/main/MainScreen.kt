package com.zen.nottwitter.presentation.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

class MainScreen : BaseScreen<MainViewModel, MainUIState, MainUIEffect, MainInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: MainUIEffect, navigator: Navigator) {
        when (effect) {
            MainUIEffect.Logout -> {}
        }
    }

    @Composable
    override fun OnRender(state: MainUIState, listener: MainInteractionListener) {
        Text(text = "Main")
    }
}