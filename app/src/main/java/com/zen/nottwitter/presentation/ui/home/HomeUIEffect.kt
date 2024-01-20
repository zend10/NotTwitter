package com.zen.nottwitter.presentation.ui.home

sealed interface HomeUIEffect {
    object NavigateToPostEditor : HomeUIEffect
    class ViewPost(uid: String): HomeUIEffect
    class ViewImage(imageUrl: String): HomeUIEffect
}