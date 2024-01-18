package com.zen.nottwitter.presentation.ui.landing

sealed interface LandingUIEffect {
    object FirstTimeUser : LandingUIEffect
    object UserVerificationSuccess : LandingUIEffect
    class UserVerificationFailed(val errorMessage: String) : LandingUIEffect
}