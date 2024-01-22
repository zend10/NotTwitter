package com.zen.nottwitter.presentation.ui.profile

import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.data.repository.ContentRepository
import com.zen.nottwitter.data.repository.UserRepository
import com.zen.nottwitter.domain.usecase.GetLocalUserPostsUseCase
import com.zen.nottwitter.domain.usecase.GetUserPostsRequest
import com.zen.nottwitter.domain.usecase.GetUserPostsUserCase
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository,
    private val configRepository: ConfigRepository,
    private val getUserPostsUserCase: GetUserPostsUserCase,
    private val getLocalUserPostsUseCase: GetLocalUserPostsUseCase,
    dispatchers: DispatcherProvider
) :
    BaseViewModel<ProfileUIState, ProfileUIEffect>(ProfileUIState(), dispatchers),
    ProfileInteractionListener {

    init {
        listenToUser()
        listenToNewPost()
    }

    private fun listenToUser() {
        screenModelScope.launch(dispatchers.io) {
            userRepository.user.collect { user ->
                updateState { it.copy(nickname = user.nickname) }
                loadLocalPosts()
                loadPosts()
            }
        }
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
            val posts = getLocalUserPostsUseCase.execute(Unit)
            updateState { it.copy(posts = posts) }
        }
    }

    private fun loadPosts() {
        screenModelScope.launch(dispatchers.io) {
            try {
                updateState { it.copy(isLoading = true) }
                val posts = getUserPostsUserCase.execute(GetUserPostsRequest(false))
                updateState { it.copy(posts = posts) }
            } catch (exception: Exception) {
                // do nothing
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    override fun onLogoutClick() {
        updateState { it.copy(showLogoutDialog = true) }
    }

    override fun onPostImageClick(imageUrl: String) {
        sendNewEffect(ProfileUIEffect.ViewImage(imageUrl))
    }

    override fun onRefresh() {
        loadPosts()
    }

    override fun onLoadNextPage() {
        val paginationPerPageLimit = configRepository.getPaginationPerPageLimit()
        if (state.value.isLoadingNextPage || state.value.posts.size < paginationPerPageLimit)
            return

        screenModelScope.launch(dispatchers.io) {
            try {
                updateState { it.copy(isLoadingNextPage = true) }
                val posts = getUserPostsUserCase.execute(GetUserPostsRequest(true))
                val newPosts = ArrayList(state.value.posts + posts)
                updateState { it.copy(posts = newPosts) }
            } catch (exception: Exception) {
                // do nothing
            } finally {
                updateState { it.copy(isLoadingNextPage = false) }
            }
        }
    }

    override fun onLogoutDialogDismiss() {
        updateState { it.copy(showLogoutDialog = false) }
    }

    override fun onLogoutDialogPositiveCtaClick() {
        screenModelScope.launch(dispatchers.io) {
            userRepository.logout()
        }
    }
}