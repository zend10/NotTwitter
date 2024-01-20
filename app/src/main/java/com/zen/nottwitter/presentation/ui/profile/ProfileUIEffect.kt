package com.zen.nottwitter.presentation.ui.profile

sealed interface ProfileUIEffect {
    object Logout : ProfileUIEffect
}