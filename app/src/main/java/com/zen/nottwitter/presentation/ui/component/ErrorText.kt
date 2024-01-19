package com.zen.nottwitter.presentation.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorText(text: String, modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Preview
@Composable
private fun ErrorTextPreview() {
    ErrorText(text = "Hello, World!")
}