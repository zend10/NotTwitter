package com.zen.nottwitter.presentation.ui.editor

data class EditorUIState(
    val isLoading: Boolean = false,
    val message: String = "",
    val imageUriString: String = "",
    val characterLimit: String = "",
    val isOverCharacterLimit: Boolean = false,
    val isPostButtonEnable: Boolean = false,
    val showBackDialog: Boolean = false,
    val showPostDialog: Boolean = false,
    val errorMessage: String = "",
    val showImagePicker: Boolean = false,
)
