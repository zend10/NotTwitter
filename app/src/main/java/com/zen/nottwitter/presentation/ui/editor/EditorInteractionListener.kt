package com.zen.nottwitter.presentation.ui.editor

import com.zen.nottwitter.presentation.ui.base.BaseInteractionListener

interface EditorInteractionListener : BaseInteractionListener {
    fun onBackButtonClick()
    fun onPostClick()
    fun onMessageChange(message: String)
    fun onAddImageClick()
    fun onBackDialogDismiss()
    fun onBackDialogPositiveCtaClick()
    fun onPostDialogDismiss()
    fun onPostDialogPositiveCtaClick()
    fun onPostErrorDialogDismiss()
}