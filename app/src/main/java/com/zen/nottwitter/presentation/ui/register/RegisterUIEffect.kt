package com.zen.nottwitter.presentation.ui.register

sealed interface RegisterUIEffect {
    object RegisterSuccess : RegisterUIEffect
}