package com.zen.nottwitter.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun ActionTopBar(
    title: String,
    actionItem: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    showAppIcon: Boolean = false
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showAppIcon)
                    AppIcon(modifier = Modifier.fillMaxHeight().width(48.dp).padding(end = 16.dp))
                if (title.isNotBlank())
                    Text(text = title)
            }
        },
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
    ActionTopBar(title = "Hello, World!", {}, showAppIcon = true)
}