package com.zen.nottwitter.presentation.ui.editor

sealed interface EditorUIEffect {
    object NavigateBack : EditorUIEffect
    class ViewImage(imageUriString: String): EditorUIEffect
}