package com.zen.nottwitter.presentation.ui.viewer

import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider

class ViewerViewModel(private val param: ViewerParam, dispatchers: DispatcherProvider) :
    BaseViewModel<ViewerUIState, ViewerUIEffect>(ViewerUIState(), dispatchers),
    ViewerInteractionListener {

    init {
        updateState { it.copy(imageLink = param.imageLink) }
    }

    override fun onBackButtonClick() {
        sendNewEffect(ViewerUIEffect.NavigateBack)
    }
}