package com.zen.nottwitter.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.zen.nottwitter.R

@Composable
fun AppIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null,
        modifier = modifier
    )
}

@Preview
@Composable
private fun AppIconPreview() {
    AppIcon()
}