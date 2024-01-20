package com.zen.nottwitter.presentation.ui.profile

import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider

class ProfileViewModel(dispatchers: DispatcherProvider) :
    BaseViewModel<ProfileUIState, ProfileUIEffect>(ProfileUIState(), dispatchers),
    ProfileInteractionListener {
}