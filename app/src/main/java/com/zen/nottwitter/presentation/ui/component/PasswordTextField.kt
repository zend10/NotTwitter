package com.zen.nottwitter.presentation.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.zen.nottwitter.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    text: String,
    onValueChange: (newText: String) -> Unit,
    isPasswordVisible: Boolean,
    onTrailingIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    GeneralTextField(
        text = text,
        onValueChange = onValueChange,
        modifier = modifier,
        labelText = stringResource(id = R.string.password),
        placeholderText = stringResource(id = R.string.password_placeholder),
        trailingIcon = {
            val icon = if (isPasswordVisible)
                R.drawable.baseline_visibility_24
            else
                R.drawable.baseline_visibility_off_24
            IconButton(onClick = onTrailingIconClick) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = if (isPasswordVisible)
                        stringResource(id = R.string.hide_password)
                    else
                        stringResource(id = R.string.show_password)
                )
            }
        },
        visualTransformation = if (isPasswordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        keyboardType = KeyboardType.Password,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1
    )
}

@Preview
@Composable
private fun PasswordTextFieldPreview() {
    PasswordTextField("Hello, World!", {}, false, {})
}