package com.zen.nottwitter.presentation.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun GeneralAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    positiveCta: String,
    negativeCta: String? = null,
    onPositiveCtaClick: (() -> Unit)? = null,
    onNegativeCtaClick: (() -> Unit)? = null
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    if (onPositiveCtaClick != null)
                        onPositiveCtaClick()

                    onDismissRequest()
                }
            ) {
                Text(text = positiveCta)
            }
        },
        dismissButton = {
            if (!negativeCta.isNullOrBlank()) {
                TextButton(
                    onClick = {
                        if (onNegativeCtaClick != null)
                            onNegativeCtaClick()

                        onDismissRequest()
                    }
                ) {
                    Text(text = negativeCta)
                }
            }
        }
    )
}