package com.zen.nottwitter.presentation.ui.landing

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LandingViewModel(private val userRepository: UserRepository) :
    BaseViewModel<LandingUIState, LandingUIEffect>(LandingUIState()),
    LandingInteractionListener {

    init {
        loadUser()
    }

    private fun loadUser() {
        screenModelScope.launch(Dispatchers.IO) {
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