package com.zen.nottwitter.presentation.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.AppIcon
import com.zen.nottwitter.presentation.ui.component.EmailTextField
import com.zen.nottwitter.presentation.ui.component.ErrorText
import com.zen.nottwitter.presentation.ui.component.LoadingBarrier
import com.zen.nottwitter.presentation.ui.component.PasswordTextField
import com.zen.nottwitter.presentation.ui.component.PrimaryButton
import com.zen.nottwitter.presentation.ui.main.MainScreen
import com.zen.nottwitter.presentation.ui.register.RegisterScreen

class LoginScreen :
    BaseScreen<LoginViewModel, LoginUIState, LoginUIEffect, LoginInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: LoginUIEffect, navigator: Navigator) {
        when (effect) {
            LoginUIEffect.LoginSuccess -> navigator.replaceAll(MainScreen())
            LoginUIEffect.NavigateToRegister -> navigator.push(RegisterScreen())
        }
    }

    @Composable
    override fun OnRender(state: LoginUIState, listener: LoginInteractionListener) {
        val focusManager = LocalFocusManager.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AppIcon(modifier = Modifier.size(100.dp))
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
                keyboardActions = KeyboardActions(onGo = { listener.onLoginClick() })
            )
            Column(
                horizontalAlignment = Alignment.End, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                TextButton(onClick = listener::onRegisterClick) {
                    Text(text = stringResource(id = R.string.register_here))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ErrorText(
                text = state.errorMessage,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            PrimaryButton(
                onClick = listener::onLoginClick,
                text = stringResource(id = R.string.login),
                modifier = Modifier.fillMaxWidth(),
                isEnabled = state.isLoginButtonEnabled
            )
        }
        if (state.isLoading) {
            focusManager.clearFocus()
            LoadingBarrier()
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun LoginScreenPreview() {
        OnRender(state = LoginUIState(), listener = object : LoginInteractionListener {
            override fun onEmailChange(email: String) {
                TODO("Not yet implemented")
            }

            override fun onPasswordChange(password: String) {
                TODO("Not yet implemented")
            }

            override fun onLoginClick() {
                TODO("Not yet implemented")
            }

            override fun onPasswordTrailingIconClick() {
                TODO("Not yet implemented")
            }

            override fun onRegisterClick() {
                TODO("Not yet implemented")
            }
        })
    }
}