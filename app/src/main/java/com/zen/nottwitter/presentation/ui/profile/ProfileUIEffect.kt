package com.zen.nottwitter.presentation.ui.profile

sealed interface ProfileUIEffect {
    class ViewImage(val imageUrl: String): ProfileUIEffect
}