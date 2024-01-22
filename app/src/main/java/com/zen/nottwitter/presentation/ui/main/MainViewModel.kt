package com.zen.nottwitter.presentation.ui.main

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository, dispatchers: DispatcherProvider) :
    BaseViewModel<MainUIState, MainUIEffect>(MainUIState(), dispatchers), MainInteractionListener {

    init {
        listenToLogoutTrigger()
    }

    private fun listenToLogoutTrigger() {
        screenModelScope.launch(dispatchers.io) {
            userRepository.logoutTrigger.collect {
                sendNewEffect(MainUIEffect.Logout)
            }
        }
    }
}