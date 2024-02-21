package com.zen.nottwitter.presentation.ui.home

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User

data class HomeUIState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val posts: List<Post> = listOf(),
    val isLoadingNextPage: Boolean = false,
    val errorMessage: String = "",
    val deleteDialog: Boolean = false
)