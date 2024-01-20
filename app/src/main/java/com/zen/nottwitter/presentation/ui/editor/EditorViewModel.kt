package com.zen.nottwitter.presentation.ui.editor

import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider

class EditorViewModel(
    private val configRepository: ConfigRepository,
    dispatchers: DispatcherProvider
) : BaseViewModel<EditorUIState, EditorUIEffect>(EditorUIState(), dispatchers),
    EditorInteractionListener {

    init {
        onMessageChange("")
    }

    override fun onBackButtonClick() {
        if (state.value.message.trim().isNotBlank()) {
            updateState { it.copy(showBackDialog = true) }
        } else {
            sendNewEffect(EditorUIEffect.NavigateBack)
        }
    }

    override fun onPostClick() {
        updateState { it.copy(showPostDialog = true) }
    }

    override fun onMessageChange(message: String) {
        updateState {
            it.copy(
                message = message,
                characterLimit = getCharacterLimitText(message),
                isOverCharacterLimit = isOverCharacterLimit(message),
                isPostButtonEnable = isPostButtonEnable(message)
            )
        }
    }

    override fun onAddImageClick() {
        // open file picker
    }

    override fun onBackDialogDismiss() {
        updateState { it.copy(showBackDialog = false) }
    }

    override fun onBackDialogPositiveCtaClick() {
        sendNewEffect(EditorUIEffect.NavigateBack)
    }

    override fun onPostDialogDismiss() {
        updateState { it.copy(showPostDialog = false) }
    }

    override fun onPostDialogPositiveCtaClick() {
        // send to firebase
    }

    override fun onPostErrorDialogDismiss() {
        updateState { it.copy(errorMessage = "") }
    }

    private fun getCharacterLimitText(message: String): String {
        val postConfig = configRepository.getPostConfig()
        return "${message.trim().length} / ${postConfig.messageMaxLength}"
    }

    private fun isOverCharacterLimit(message: String): Boolean {
        val postConfig = configRepository.getPostConfig()
        return message.trim().length > postConfig.messageMaxLength
    }

    private fun isPostButtonEnable(message: String): Boolean {
        val postConfig = configRepository.getPostConfig()
        val trimmedMessage = message.trim()
        return trimmedMessage.isNotBlank() && trimmedMessage.length <= postConfig.messageMaxLength
    }
}