package com.zen.nottwitter.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zen.nottwitter.R

@ExperimentalMaterial3Api
@Composable
fun BackTopBar(title: String, onBackButtonPressed: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackButtonPressed) {
                Icon(Icons.Default.ArrowBack, stringResource(id = R.string.back_button))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BackTopBarPreview() {
    BackTopBar("Hello, World!", {})
}