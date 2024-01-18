package com.zen.nottwitter.presentation.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform

@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
abstract class BaseScreen<VM, S, E, I> : Screen where I : BaseInteractionListener, VM : BaseViewModel<S, E>, VM : I {

    @Composable
    protected fun Init(viewModel: VM) {
        val state: S by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        Listen(viewModel.effect) { onEffect(it, navigator) }
        OnRender(state, viewModel)
    }

    @Composable
    private fun Listen(effect: Flow<E>, function: (E) -> Unit) {
        LaunchedEffect(Unit) {
            effect.collectLatest {
                it?.let { function(it) }
            }
        }
    }

    abstract fun onEffect(effect: E, navigator: Navigator)

    @Composable
    abstract fun OnRender(state: S, listener: I)

    @Composable
    protected inline fun <reified T : ScreenModel> getViewModel(vararg params: Any): T {
        val koin = KoinPlatform.getKoin()
        return rememberScreenModel { koin.get { parametersOf(params) } }
    }
}