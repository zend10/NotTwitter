package com.zen.nottwitter.presentation.ui.base

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.zen.nottwitter.domain.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, E>(initialState: S) : StateScreenModel<S>(initialState), BaseInteractionListener {

    private val _effect = MutableSharedFlow<E?>()
    val effect = _effect.asSharedFlow().throttleFirst(500).mapNotNull { it }

    protected fun updateState(updater: (S) -> S) {
        mutableState.update(updater)
    }

    protected fun sendNewEffect(newEffect: E) {
        screenModelScope.launch(Dispatchers.IO) {
            _effect.emit(newEffect)
        }
    }

    private fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
        require(periodMillis > 0)
        return flow {
            var lastTime = 0L
            collect { value ->
                val currentTime = TimeUtils.getCurrentMillis()
                if (currentTime - lastTime >= periodMillis) {
                    lastTime = currentTime
                    emit(value)
                }
            }
        }
    }
 }