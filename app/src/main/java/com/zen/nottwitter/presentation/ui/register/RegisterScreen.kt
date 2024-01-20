package com.zen.nottwitter.presentation.ui.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.BackTopBar
import com.zen.nottwitter.presentation.ui.component.EmailTextField
import com.zen.nottwitter.presentation.ui.component.ErrorText
import com.zen.nottwitter.presentation.ui.component.GeneralTextField
import com.zen.nottwitter.presentation.ui.component.LoadingBarrier
import com.zen.nottwitter.presentation.ui.component.PasswordTextField
import com.zen.nottwitter.presentation.ui.component.PrimaryButton
import com.zen.nottwitter.presentation.ui.main.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
class RegisterScreen :
    BaseScreen<RegisterViewModel, RegisterUIState, RegisterUIEffect, RegisterInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: RegisterUIEffect, navigator: Navigator) {
        when (effect) {
            RegisterUIEffect.RegisterSuccess -> navigator.replaceAll(MainScreen())
            RegisterUIEffect.NavigateBack -> navigator.pop()
        }
    }

    @Composable
    override fun OnRender(state: RegisterUIState, listener: RegisterInteractionListener) {
        val focusManager = LocalFocusManager.current

        Scaffold(
            topBar = {
                BackTopBar(
                    title = stringResource(id = R.string.register),
                    onBackButtonPressed = { listener.onBackButtonClick() })
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
                    text = state.nickname,
                    onValueChange = listener::onNicknameChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    labelText = stringResource(id = R.string.nickname),
                    placeholderText = stringResource(id = R.string.nickname_placeholder),
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
                EmailTextField(
                    text = state.email,
                    onValueChange = listener::onEmailChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    imeAction = ImeAction.Next
                )
                PasswordTextField(
                    text = state.password,
                    onValueChange = listener::onPasswordChange,
                    isPasswordVisible = state.isPasswordVisible,
                    onTrailingIconClick = listener::onPasswordTrailingIconClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    imeAction = ImeAction.Go,
                    keyboardActions = KeyboardActions(onGo = { listener.onRegisterClick() })
                )
                Spacer(modifier = Modifier.weight(1f))
                ErrorText(
                    text = state.errorMessage,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                PrimaryButton(
                    onClick = listener::onRegisterClick,
                    text = stringResource(id = R.string.register),
                    modifier = Modifier.fillMaxWidth(),
                    isEnabled = state.isRegisterButtonEnabled
                )
            }
        }
        if (state.isLoading) {
            focusManager.clearFocus()
            LoadingBarrier()
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun RegisterScreenPreview() {
        OnRender(
            state = RegisterUIState(isLoading = true),
            listener = object : RegisterInteractionListener {
                override fun onEmailChange(email: String) {
                    TODO("Not yet implemented")
                }

                override fun onPasswordChange(password: String) {
                    TODO("Not yet implemented")
                }

                override fun onPasswordTrailingIconClick() {
                    TODO("Not yet implemented")
                }

                override fun onRegisterClick() {
                    TODO("Not yet implemented")
                }

                override fun onBackButtonClick() {
                    TODO("Not yet implemented")
                }

                override fun onNicknameChange(nickname: String) {
                    TODO("Not yet implemented")
                }
            })
    }
}