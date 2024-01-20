package com.zen.nottwitter.presentation.ui.editor

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import coil.compose.AsyncImage
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.BackTopBar
import com.zen.nottwitter.presentation.ui.component.GeneralAlertDialog
import com.zen.nottwitter.presentation.ui.component.GeneralTextField
import com.zen.nottwitter.presentation.ui.component.LoadingBarrier

@OptIn(ExperimentalMaterial3Api::class)
class EditorScreen :
    BaseScreen<EditorViewModel, EditorUIState, EditorUIEffect, EditorInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: EditorUIEffect, navigator: Navigator) {
        when (effect) {
            EditorUIEffect.NavigateBack -> navigator.pop()
            is EditorUIEffect.ViewImage -> {}
        }
    }

    @Composable
    override fun OnRender(state: EditorUIState, listener: EditorInteractionListener) {
        val focusManager = LocalFocusManager.current
        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> listener.onImageSelected(uri?.toString()) }
        )

        Scaffold(
            topBar = {
                BackTopBar(
                    title = stringResource(id = R.string.new_post),
                    onBackButtonPressed = listener::onBackButtonClick,
                    actionItem = {
                        TextButton(
                            onClick = listener::onPostClick,
                            enabled = state.isPostButtonEnable
                        ) {
                            Text(text = stringResource(id = R.string.post))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                GeneralTextField(
                    text = state.message,
                    onValueChange = listener::onMessageChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholderText = stringResource(id = R.string.post_placeholder),
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Go,
                    keyboardActions = KeyboardActions(onGo = { listener.onPostClick() }),
                    singleLine = false,
                    maxLines = Int.MAX_VALUE
                )
                UtilityBar(state, listener)
            }
        }

        if (state.isLoading) {
            focusManager.clearFocus()
            LoadingBarrier()
        }

        if (state.showImagePicker) {
            imagePickerLauncher
                .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        AlertDialog(state, listener)
    }

    @Composable
    private fun UtilityBar(state: EditorUIState, listener: EditorInteractionListener) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (state.imageUriString.isNotBlank()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(onClick = { listener.onRemoveImageClick() }) {
                        Text(text = stringResource(id = R.string.remove_image))
                    }
                    AsyncImage(
                        model = state.imageUriString,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { listener.onImageClick() },
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                TextButton(onClick = { listener.onAddImageClick() }) {
                    Text(text = stringResource(id = R.string.add_image))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = state.characterLimit,
                color = if (state.isOverCharacterLimit)
                    MaterialTheme.colorScheme.error
                else
                    Color.Unspecified,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }

    @Composable
    private fun AlertDialog(state: EditorUIState, listener: EditorInteractionListener) {
        if (state.showBackDialog) {
            GeneralAlertDialog(
                onDismissRequest = listener::onBackDialogDismiss,
                title = stringResource(R.string.back_dialog_title),
                message = stringResource(R.string.back_dialog_message),
                positiveCta = stringResource(R.string.leave),
                negativeCta = stringResource(R.string.cancel),
                onPositiveCtaClick = listener::onBackDialogPositiveCtaClick
            )
        } else if (state.showPostDialog) {
            GeneralAlertDialog(
                onDismissRequest = listener::onPostDialogDismiss,
                title = stringResource(R.string.post_dialog_title),
                message = stringResource(R.string.post_dialog_message),
                positiveCta = stringResource(id = R.string.post),
                negativeCta = stringResource(id = R.string.cancel),
                onPositiveCtaClick = listener::onPostDialogPositiveCtaClick
            )
        } else if (state.errorMessage.isNotBlank()) {
            GeneralAlertDialog(
                onDismissRequest = listener::onPostErrorDialogDismiss,
                title = stringResource(R.string.post_error_dialog_title),
                message = state.errorMessage,
                positiveCta = stringResource(R.string.ok),
            )
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun EditorScreenPreview() {
        OnRender(state = EditorUIState(), listener = object : EditorInteractionListener {
            override fun onBackButtonClick() {
                TODO("Not yet implemented")
            }

            override fun onPostClick() {
                TODO("Not yet implemented")
            }

            override fun onMessageChange(message: String) {
                TODO("Not yet implemented")
            }

            override fun onAddImageClick() {
                TODO("Not yet implemented")
            }

            override fun onRemoveImageClick() {
                TODO("Not yet implemented")
            }

            override fun onImageClick() {
                TODO("Not yet implemented")
            }

            override fun onBackDialogDismiss() {
                TODO("Not yet implemented")
            }

            override fun onBackDialogPositiveCtaClick() {
                TODO("Not yet implemented")
            }

            override fun onPostDialogDismiss() {
                TODO("Not yet implemented")
            }

            override fun onPostDialogPositiveCtaClick() {
                TODO("Not yet implemented")
            }

            override fun onPostErrorDialogDismiss() {
                TODO("Not yet implemented")
            }

            override fun onImageSelected(uriString: String?) {
                TODO("Not yet implemented")
            }
        })
    }
}