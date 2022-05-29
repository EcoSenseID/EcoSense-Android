package com.ecosense.android.featAuth.presentation.registration

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.AuthNavGraph
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featAuth.presentation.component.AuthTextField
import com.ecosense.android.featAuth.presentation.component.EmailTextField
import com.ecosense.android.featAuth.presentation.component.PasswordTextField
import com.ecosense.android.featAuth.presentation.login.contract.GoogleSignInContract
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
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = GoogleSignInContract
    ) { idToken -> viewModel.onGoogleSignInResult(idToken = idToken) }
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
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.registration_screen_title),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = stringResource(R.string.registration_screen_caption),
                style = MaterialTheme.typography.body1,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AuthTextField(
                value = state.name,
                onValueChange = { viewModel.onNameValueChange(it) },
                label = { Text(text = stringResource(id = R.string.name)) },
                placeholder = { Text(text = stringResource(id = R.string.name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )

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

            RoundedEndsButton(
                enabled = !state.isLoading,
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        if (state.isRegistering) R.string.registering_an_account
                        else R.string.register
                    )
                )
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

            RoundedEndsButton(
                enabled = !state.isLoading,
                onClick = { googleSignInLauncher.launch(0) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.surface,
                )
            ) {

                AsyncImage(
                    model = R.drawable.ic_google,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                Text(
                    text = stringResource(
                        if (state.isLoading) R.string.logging_in
                        else R.string.continue_with_google
                    ),
                    color = MaterialTheme.colors.primary,
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
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navigator.popBackStack() }
                )
            }
        }
    }
}