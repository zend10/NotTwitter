package com.zen.nottwitter.presentation.ui.home

import com.zen.nottwitter.data.model.Post

data class HomeUIState(
    val isLoading: Boolean = false,
    val posts: List<Post> = listOf(),
    val isLoadingNextPage: Boolean = false
)