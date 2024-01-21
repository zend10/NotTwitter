package com.zen.nottwitter.presentation.ui.profile

import com.zen.nottwitter.data.model.Post

data class ProfileUIState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val posts: List<Post> = listOf(),
    val isLoadingNextPage: Boolean = false,
    val showLogoutDialog: Boolean = false
)
