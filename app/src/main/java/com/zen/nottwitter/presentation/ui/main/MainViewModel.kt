package com.zen.nottwitter.presentation.ui.main

import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider

class MainViewModel(dispatchers: DispatcherProvider) :
    BaseViewModel<MainUIState, MainUIEffect>(MainUIState(), dispatchers), MainInteractionListener {
}