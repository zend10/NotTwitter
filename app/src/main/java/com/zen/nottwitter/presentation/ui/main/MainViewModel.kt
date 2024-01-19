package com.zen.nottwitter.presentation.ui.main

import com.zen.nottwitter.presentation.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<MainUIState, MainUIEffect>(MainUIState()), MainInteractionListener {
}