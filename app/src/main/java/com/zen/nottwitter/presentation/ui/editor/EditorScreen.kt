package com.zen.nottwitter.presentation.ui.editor

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.presentation.ui.base.BaseScreen

class EditorScreen : BaseScreen<EditorViewModel, EditorUIState, EditorUIEffect, EditorInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: EditorUIEffect, navigator: Navigator) {
        when (effect) {
            else -> {}
        }
    }
    
    @Composable
    override fun OnRender(state: EditorUIState, listener: EditorInteractionListener) {
        Text(text = "Editor")
    }
    
    @Preview(showSystemUi = true)
    @Composable
    private fun EditorScreenPreview() {
        OnRender(state = EditorUIState(), listener = object : EditorInteractionListener {
            
        })
    }
}