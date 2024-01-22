package com.zen.nottwitter.presentation.ui.main

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository,
    dispatchers: DispatcherProvider
) :
    BaseViewModel<MainUIState, MainUIEffect>(MainUIState(), dispatchers), MainInteractionListener {

    init {
        listenToLogoutTrigger()
        listenToSendingPost()
    }

    private fun listenToSendingPost() {
        screenModelScope.launch(dispatchers.io) {
            contentRepository.sendingPost.collect { sendingPost ->
                updateState { it.copy(isLoading = sendingPost) }
            }
        }
    }

    private fun listenToLogoutTrigger() {
        screenModelScope.launch(dispatchers.io) {
            userRepository.logoutTrigger.collect {
                sendNewEffect(MainUIEffect.Logout)
            }
        }
    }
}