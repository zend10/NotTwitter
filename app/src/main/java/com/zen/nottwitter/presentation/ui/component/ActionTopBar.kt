package com.zen.nottwitter.presentation.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zen.nottwitter.R

@ExperimentalMaterial3Api
@Composable
fun ActionTopBar(title: String, actionItem: @Composable () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        actions = {
            actionItem()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ActionTopBarPreview() {
    ActionTopBar(title = "Hello, World!", {})
}