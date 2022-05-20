package com.ecosense.android.featAuth.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.AuthNavGraph
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featAuth.presentation.component.EmailTextField
import com.ecosense.android.featAuth.presentation.component.PasswordTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@AuthNavGraph
fun RegisterScreen(
    navigator: DestinationsNavigator,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(text = stringResource(R.string.register))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            EmailTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.onEmailValueChange(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PasswordTextField(
                value = viewModel.password.value,
                isVisible = viewModel.isPasswordVisible.value,
                onValueChange = { viewModel.onPasswordValueChange(it) },
                onChangeVisibility = { viewModel.onChangePasswordVisibility() }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PasswordTextField(
                value = viewModel.repeatedPassword.value,
                isVisible = viewModel.isRepeatedPasswordVisible.value,
                onValueChange = { viewModel.onRepeatedPasswordValueChange(it) },
                onChangeVisibility = { viewModel.onChangeRepeatedPasswordVisibility() },
                label = { Text(text = stringResource(R.string.repeat_password)) },
                placeholder = { Text(text = stringResource(R.string.repeat_password)) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Button(
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.register))
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