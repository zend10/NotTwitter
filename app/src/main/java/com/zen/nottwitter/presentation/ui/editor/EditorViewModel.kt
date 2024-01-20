package com.zen.nottwitter.presentation.ui.editor

import com.zen.nottwitter.presentation.ui.base.BaseViewModel
import com.zen.nottwitter.presentation.ui.base.DispatcherProvider

class EditorViewModel(dispatchers: DispatcherProvider) :
    BaseViewModel<EditorUIState, EditorUIEffect>(EditorUIState(), dispatchers),
    EditorInteractionListener {
}