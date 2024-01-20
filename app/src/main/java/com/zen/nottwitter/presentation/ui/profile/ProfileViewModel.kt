package com.zen.nottwitter.presentation.ui.profile

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    dispatchers: DispatcherProvider
) :
    BaseViewModel<ProfileUIState, ProfileUIEffect>(ProfileUIState(), dispatchers),
    ProfileInteractionListener {

    init {
        loadUser()
    }

    private fun loadUser() {
        screenModelScope.launch(dispatchers.io) {
            try {
                val user = userRepository.getUser()
                updateState { it.copy(nickname = user.nickname) }
            } catch (_: NoSuchElementException) {
                // do nothing
            }
        }
    }

    override fun onLogoutClick() {
        screenModelScope.launch(dispatchers.io) {
            userRepository.logout()
            sendNewEffect(ProfileUIEffect.Logout)
        }
    }
}