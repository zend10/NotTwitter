package com.zen.nottwitter.presentation.ui.home

import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider

class HomeViewModel(dispatchers: DispatcherProvider) :
    BaseViewModel<HomeUIState, HomeUIEffect>(HomeUIState(), dispatchers), HomeInteractionListener {

    override fun onNewPostClick() {

    }
}