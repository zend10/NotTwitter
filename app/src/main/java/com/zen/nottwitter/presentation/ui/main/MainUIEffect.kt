package com.zen.nottwitter.presentation.ui.main

sealed interface MainUIEffect {
    object Logout : MainUIEffect
}