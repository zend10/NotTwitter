package com.zen.nottwitter.presentation.ui.login

import com.zen.nottwitter.presentation.ui.base.BaseViewModel

class LoginViewModel : BaseViewModel<LoginUIState, LoginUIEffect>(LoginUIState()), LoginInteractionListener {
}