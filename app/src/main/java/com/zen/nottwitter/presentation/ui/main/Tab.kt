package com.zen.nottwitter.presentation.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.home.HomeScreen
import com.zen.nottwitter.presentation.ui.profile.ProfileScreen

object HomeTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = stringResource(id = R.string.home)
            return remember { TabOptions(index = 0u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = HomeScreen())
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = stringResource(id = R.string.profile)
            return remember { TabOptions(index = 1u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = ProfileScreen())
    }
}

@Composable
fun rememberTabsContainer(): List<TabContainer> {
    return remember {
        listOf(
            TabContainer(
                HomeTab,
                Icons.Default.Home,
                Icons.Outlined.Home
            ),
            TabContainer(
                ProfileTab,
                Icons.Default.Person,
                Icons.Outlined.Person
            )
        )
    }
}

data class TabContainer(
    val tab: Tab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)