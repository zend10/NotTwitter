package com.zen.nottwitter.presentation.ui.landing

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LandingViewModel : BaseViewModel<LandingUIState, LandingUIEffect>(LandingUIState()), LandingInteractionListener {

    init {
        loadUser()
    }

    private fun loadUser() {
        screenModelScope.launch(Dispatchers.IO) {
            delay(2000)
            sendNewEffect(LandingUIEffect.FirstTimeUser)
        }
    }
}