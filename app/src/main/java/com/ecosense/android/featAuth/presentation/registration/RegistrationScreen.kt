package com.ecosense.android.featAuth.presentation.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.AuthNavGraph
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featAuth.presentation.component.EmailTextField
import com.ecosense.android.featAuth.presentation.component.PasswordTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
@AuthNavGraph
fun RegistrationScreen(
    navigator: DestinationsNavigator,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    focusManager.clearFocus()
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }

                is UIEvent.HideKeyboard -> {
                    focusManager.clearFocus()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(text = stringResource(R.string.register))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            EmailTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailValueChange(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PasswordTextField(
                value = state.password,
                isVisible = state.isPasswordVisible,
                onValueChange = { viewModel.onPasswordValueChange(it) },
                onToggleVisibility = { viewModel.onTogglePasswordVisibility() }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PasswordTextField(
                value = state.repeatedPassword,
                isVisible = state.isRepeatedPasswordVisible,
                onValueChange = { viewModel.onRepeatedPasswordValueChange(it) },
                onToggleVisibility = { viewModel.onToggleRepeatedPasswordVisibility() },
                label = { Text(text = stringResource(R.string.repeat_password)) },
                placeholder = { Text(text = stringResource(R.string.repeat_password)) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Button(
                enabled = !state.isLoading,
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        if (state.isLoading) R.string.registering_an_account
                        else R.string.register
                    )
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.already_have_an_account).plus(" "))
                Text(
                    text = stringResource(R.string.login),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { navigator.popBackStack() }
                )
            }
        }
    }
}