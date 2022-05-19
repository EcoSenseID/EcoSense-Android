package com.ecosense.android.featAuth.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.AuthNavGraph
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.RegisterScreenDestination
import com.ecosense.android.featAuth.presentation.contract.GoogleSignInContract
import com.ecosense.android.featAuth.presentation.component.EmailTextField
import com.ecosense.android.featAuth.presentation.component.PasswordTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@AuthNavGraph(start = true)
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = GoogleSignInContract
    ) { idToken -> viewModel.onGoogleSignInResult(idToken = idToken) }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(text = stringResource(id = R.string.login))

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

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { viewModel.onForgotPasswordClick() }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Button(
                onClick = { viewModel.onLoginClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.or).uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
                )

                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Button(
                onClick = { googleSignInLauncher.launch(0) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.login_with_google))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.new_here).plus(" "))
                Text(
                    text = stringResource(R.string.create_an_account),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { navigator.navigate(RegisterScreenDestination) }
                )
            }
        }
    }
}