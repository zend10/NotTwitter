package com.zen.nottwitter.presentation.ui.viewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import coil.compose.AsyncImage
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.BackTopBar
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
data class ViewerScreen(private val imageLink: String) :
    BaseScreen<ViewerViewModel, ViewerUIState, ViewerUIEffect, ViewerInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel() { parametersOf(ViewerParam(imageLink)) })
    }

    override fun onEffect(effect: ViewerUIEffect, navigator: Navigator) {
        when (effect) {
            ViewerUIEffect.NavigateBack -> navigator.pop()
        }
    }

    @Composable
    override fun OnRender(state: ViewerUIState, listener: ViewerInteractionListener) {
        Scaffold(
            topBar = {
                BackTopBar(title = "", onBackButtonPressed = listener::onBackButtonClick)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = state.imageLink,
                    contentDescription = null
                )
            }
        }
    }
}