package com.zen.nottwitter.presentation.ui.viewer

sealed interface ViewerUIEffect {
    object NavigateBack : ViewerUIEffect
}