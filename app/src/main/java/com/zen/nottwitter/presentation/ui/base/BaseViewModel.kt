package com.zen.nottwitter.presentation.ui.base

import cafe.adriel.voyager.core.model.StateScreenModel

abstract class BaseViewModel<S, E>(initialState: S) : StateScreenModel<S>(initialState), BaseInteractionListener {

}