package com.zen.nottwitter.presentation.ui.profile

import com.zen.nottwitter.presentation.ui.base.BaseInteractionListener

interface ProfileInteractionListener : BaseInteractionListener {
    fun onLogoutClick()
    fun onPostImageClick(imageUrl: String)
    fun onRefresh()
    fun onLoadNextPage()
    fun onLogoutDialogDismiss()
    fun onLogoutDialogPositiveCtaClick()
}