package com.zen.nottwitter.presentation.ui.home

sealed interface HomeUIEffect {
    object NavigateToPostEditor : HomeUIEffect
    class ViewPost(val uid: String) : HomeUIEffect
    class ViewImage(val imageUrl: String) : HomeUIEffect
}