package com.zen.nottwitter.presentation.ui.landing

sealed interface LandingUIEffect {
    object FirstTimeUser : LandingUIEffect
    object AuthenticationSuccess : LandingUIEffect
    object AuthenticationFailed : LandingUIEffect
}