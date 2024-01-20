package com.zen.nottwitter.presentation.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

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
        Text(text = "Home")
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun HomeScreenPreview() {
        OnRender(state = HomeUIState(), listener = object : HomeInteractionListener {

        })
    }
}