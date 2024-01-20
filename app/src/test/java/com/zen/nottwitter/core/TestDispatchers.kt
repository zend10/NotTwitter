package com.zen.nottwitter.core

import com.zen.nottwitter.presentation.ui.base.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchers : DispatcherProvider {
    val testDispatcher = UnconfinedTestDispatcher()
    override val ui: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
}