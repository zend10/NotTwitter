package com.zen.nottwitter.presentation.ui.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

class ProfileScreen :
    BaseScreen<ProfileViewModel, ProfileUIState, ProfileUIEffect, ProfileInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: ProfileUIEffect, navigator: Navigator) {
        when (effect) {
            else -> {}
        }
    }

    @Composable
    override fun OnRender(state: ProfileUIState, listener: ProfileInteractionListener) {
        Text(text = "Profile")
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun ProfileScreenPreview() {
        OnRender(state = ProfileUIState(), listener = object : ProfileInteractionListener {

        })
    }
}