package com.zen.nottwitter.presentation.ui.profile

sealed interface ProfileUIEffect {
    object Logout : ProfileUIEffect
    class ViewImage(val imageUrl: String): ProfileUIEffect
}