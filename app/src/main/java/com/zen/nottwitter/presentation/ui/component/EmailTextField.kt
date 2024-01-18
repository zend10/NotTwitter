package com.zen.nottwitter.presentation.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.zen.nottwitter.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(
    text: String,
    onValueChange: (newText: String) -> Unit,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    GeneralTextField(
        text = text,
        onValueChange = onValueChange,
        modifier = modifier,
        labelText = stringResource(id = R.string.email),
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1
    )
}

@Preview
@Composable
private fun EmailTextFieldPreview() {
    EmailTextField("Hello, World!", {})
}