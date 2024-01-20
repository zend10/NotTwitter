package com.zen.nottwitter.presentation.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.ActionTopBar
import com.zen.nottwitter.presentation.ui.component.EmptyScreen

@OptIn(ExperimentalMaterial3Api::class)
class HomeScreen : BaseScreen<HomeViewModel, HomeUIState, HomeUIEffect, HomeInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: HomeUIEffect, navigator: Navigator) {
        when (effect) {
            else -> {}
        }
    }

    @Composable
    override fun OnRender(state: HomeUIState, listener: HomeInteractionListener) {
        Scaffold(
            topBar = {
                ActionTopBar(
                    title = "",
                    actionItem = {
                        IconButton(onClick = listener::onNewPostClick) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = stringResource(
                                    id = R.string.new_post
                                )
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            EmptyScreen(modifier = Modifier.padding(paddingValues))
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun HomeScreenPreview() {
        OnRender(state = HomeUIState(), listener = object : HomeInteractionListener {
            override fun onNewPostClick() {
                TODO("Not yet implemented")
            }
        })
    }
}