package com.zen.nottwitter.presentation.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.ActionTopBar
import com.zen.nottwitter.presentation.ui.component.EmptyScreen
import com.zen.nottwitter.presentation.ui.landing.LandingScreen

@OptIn(ExperimentalMaterial3Api::class)
class ProfileScreen :
    BaseScreen<ProfileViewModel, ProfileUIState, ProfileUIEffect, ProfileInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: ProfileUIEffect, navigator: Navigator) {
        when (effect) {
            is ProfileUIEffect.Logout -> navigator.parent?.parent?.replaceAll(LandingScreen())
        }
    }

    @Composable
    override fun OnRender(state: ProfileUIState, listener: ProfileInteractionListener) {
        Scaffold(
            topBar = {
                ActionTopBar(
                    title = state.nickname,
                    actionItem = {
                        TextButton(onClick = listener::onLogoutClick) {
                            Text(text = stringResource(id = R.string.logout))
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
    private fun ProfileScreenPreview() {
        OnRender(state = ProfileUIState(), listener = object : ProfileInteractionListener {
            override fun onLogoutClick() {
                TODO("Not yet implemented")
            }
        })
    }
}