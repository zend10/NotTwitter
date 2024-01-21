package com.zen.nottwitter.presentation.ui.editor

sealed interface EditorUIEffect {
    object NavigateBack : EditorUIEffect
    object CreatePostSuccess: EditorUIEffect
    class ViewImage(val imageUriString: String): EditorUIEffect
}