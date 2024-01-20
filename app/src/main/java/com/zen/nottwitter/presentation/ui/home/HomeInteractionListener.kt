package com.zen.nottwitter.presentation.ui.home

import com.zen.nottwitter.presentation.ui.base.BaseInteractionListener

interface HomeInteractionListener : BaseInteractionListener {
    fun onNewPostClick()
    fun onPostClick(uid: String)
    fun onPostImageClick(imageUrl: String)
}