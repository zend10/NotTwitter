package com.zen.nottwitter.presentation.ui.register

sealed interface RegisterUIEffect {
    object NavigateBack : RegisterUIEffect
    object RegisterSuccess : RegisterUIEffect
}