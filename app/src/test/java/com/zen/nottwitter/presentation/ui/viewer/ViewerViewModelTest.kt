package com.zen.nottwitter.presentation.ui.viewer

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ViewerViewModelTest : BaseTest() {

    private lateinit var viewModel: ViewerViewModel
    private lateinit var state: StateFlow<ViewerUIState>

    @MockK
    private lateinit var param: ViewerParam

    @Before
    override fun setUp() {
        super.setUp()
        every { param.imageLink } returns stubTestImageUrl
        viewModel = ViewerViewModel(param, testDispatchers)
        state = viewModel.state
    }

    @Test
    fun `ViewerViewModel on init will update imageLink state`() {
        assertEquals(stubTestImageUrl, viewModel.state.value.imageLink)
    }

    @Test
    fun `onBackButtonClick will emit NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackButtonClick()
            assertEquals(ViewerUIEffect.NavigateBack, awaitItem())
        }
    }
}