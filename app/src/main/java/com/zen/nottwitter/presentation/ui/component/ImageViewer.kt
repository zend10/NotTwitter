package com.zen.nottwitter.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.zen.nottwitter.R

@Composable
fun ImageViewer(imageLink: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Close, contentDescription = stringResource(id = R.string.back_button))
        }
    }
}

@Preview
@Composable
private fun ImageViewPreview() {
    ImageViewer(imageLink = "")
}