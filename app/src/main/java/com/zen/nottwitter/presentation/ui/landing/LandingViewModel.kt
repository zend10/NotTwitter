package com.zen.nottwitter.presentation.ui.landing

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LandingViewModel(
    private val userRepository: UserRepository,
    dispatchers: DispatcherProvider
) :
    BaseViewModel<LandingUIState, LandingUIEffect>(LandingUIState(), dispatchers),
    LandingInteractionListener {

    init {
        loadUser()
    }

    private fun loadUser() {
        screenModelScope.launch(dispatchers.io) {
            try {
                delay(2000)
                val user = userRepository.authenticate()
                if (user != null) {
                    sendNewEffect(LandingUIEffect.AuthenticationSuccess)
                } else {
                    sendNewEffect(LandingUIEffect.FirstTimeUser)
                }
            } catch (exception: Exception) {
                userRepository.logout()
                sendNewEffect(LandingUIEffect.AuthenticationFailed)
            }
        }
    }
}