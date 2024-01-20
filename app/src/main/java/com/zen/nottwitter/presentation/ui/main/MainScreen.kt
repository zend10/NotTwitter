package com.zen.nottwitter.presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainScreen : BaseScreen<MainViewModel, MainUIState, MainUIEffect, MainInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: MainUIEffect, navigator: Navigator) {
        when (effect) {
            MainUIEffect.Logout -> {}
        }
    }

    @Composable
    override fun OnRender(state: MainUIState, listener: MainInteractionListener) {
        TabNavigator(HomeTab) {
            Scaffold(
                bottomBar = {
                    val tabNavigator = LocalTabNavigator.current
                    val tabs = rememberTabsContainer()
                    BottomBar(tabs = tabs, tabNavigator = tabNavigator)
                }
            ) { paddingValues ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)) {
                    CurrentTab()
                }
            }
        }
    }

    @Composable
    private fun BottomBar(tabs: List<TabContainer>, tabNavigator: TabNavigator) {
        NavigationBar {
            tabs.forEach { tabContainer ->
                val isSelected = tabNavigator.current == tabContainer.tab
                val icon = if (isSelected)
                    rememberVectorPainter(image = tabContainer.selectedIcon)
                else
                    rememberVectorPainter(image = tabContainer.unselectedIcon)
                val title = tabContainer.tab.options.title
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { tabNavigator.current = tabContainer.tab },
                    icon = { Icon(painter = icon, contentDescription = title) },
                    label = { Text(text = title) }
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun MainScreenPreview() {
        OnRender(state = MainUIState(), listener = object : MainInteractionListener {

        })
    }
}