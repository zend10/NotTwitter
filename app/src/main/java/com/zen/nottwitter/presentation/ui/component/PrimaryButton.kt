package com.zen.nottwitter.presentation.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Button(onClick = onClick, modifier = modifier, enabled = isEnabled) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    PrimaryButton(onClick = {}, text = "Hello, World!")
}