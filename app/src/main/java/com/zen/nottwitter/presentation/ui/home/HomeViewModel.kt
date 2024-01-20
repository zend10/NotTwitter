package com.zen.nottwitter.presentation.ui.home

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.launch

class HomeViewModel(dispatchers: DispatcherProvider) :
    BaseViewModel<HomeUIState, HomeUIEffect>(HomeUIState(), dispatchers), HomeInteractionListener {

    init {
        loadPosts()
    }

    private fun loadPosts() {
        screenModelScope.launch(dispatchers.io) {

        }
    }

    override fun onNewPostClick() {
        sendNewEffect(HomeUIEffect.NavigateToPostEditor)
    }

    override fun onPostClick(uid: String) {
        sendNewEffect(HomeUIEffect.ViewPost(uid))
    }

    override fun onPostImageClick(imageUrl: String) {
        sendNewEffect(HomeUIEffect.ViewImage(imageUrl))
    }
}