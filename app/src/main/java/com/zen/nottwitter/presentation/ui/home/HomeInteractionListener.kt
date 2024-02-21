package com.zen.nottwitter.presentation.ui.home

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.presentation.ui.base.BaseInteractionListener

interface HomeInteractionListener : BaseInteractionListener {
    fun onNewPostClick()
    fun onPostImageClick(imageUrl: String)
    fun onPostDeleteClick(post: Post)
    fun onPostDeleteDialogPositiveCtaClick()
    fun onPostDeleteDialogNegativeCtaClick()
    fun onRefresh()
    fun onLoadNextPage()
}