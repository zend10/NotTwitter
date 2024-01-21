package com.zen.nottwitter.presentation.ui.home

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val contentRepository: ContentRepository,
    dispatchers: DispatcherProvider
) :
    BaseViewModel<HomeUIState, HomeUIEffect>(HomeUIState(), dispatchers), HomeInteractionListener {

    init {
        listenToNewPost()
        loadLocalPosts()
        loadPosts()
    }

    private fun listenToNewPost() {
        screenModelScope.launch(dispatchers.io) {
            contentRepository.newPost.collect { newPost ->
                val posts = ArrayList(state.value.posts)
                posts.add(0, newPost)
                updateState { it.copy(posts = posts) }
            }
        }
    }

    private fun loadLocalPosts() {
        screenModelScope.launch(dispatchers.io) {
            val posts = contentRepository.getLocalPosts()
            updateState { it.copy(posts = posts) }
        }
    }

    private fun loadPosts() {
        screenModelScope.launch(dispatchers.io) {
            try {
                updateState { it.copy(isLoading = true) }
                val posts = contentRepository.getPosts()
                updateState { it.copy(posts = posts) }
            } catch (exception: Exception) {
                // do nothing
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    override fun onNewPostClick() {
        sendNewEffect(HomeUIEffect.NavigateToPostEditor)
    }

    override fun onPostClick(post: Post) {
        sendNewEffect(HomeUIEffect.ViewPost(post.uid))
    }

    override fun onPostImageClick(imageUrl: String) {
        sendNewEffect(HomeUIEffect.ViewImage(imageUrl))
    }

    override fun onRefresh() {
        loadPosts()
    }
}