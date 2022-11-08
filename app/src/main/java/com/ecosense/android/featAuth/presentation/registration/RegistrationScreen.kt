package com.ecosense.android.featAuth.presentation.registration

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featAuth.presentation.component.*
import com.ecosense.android.featAuth.presentation.login.contract.GoogleSignInContract
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalUnitApi::class)
@Composable
@Destination
fun RegistrationScreen(
    navigator: DestinationsNavigator, viewModel: RegistrationViewModel = hiltViewModel()
) {
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = GoogleSignInContract,
        onResult = { idToken -> viewModel.onGoogleSignInResult(idToken = idToken) },
    )
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
                else -> {}
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.surface)
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            AuthTopBar(onClickCancel = { navigator.navigateUp() })

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            Text(
                text = stringResource(R.string.welcome),
                fontSize = TextUnit(34f, TextUnitType.Sp),
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AuthTextField(
                value = state.name,
                onValueChange = { viewModel.onNameValueChange(it) },
                label = { Text(text = stringResource(R.string.full_name)) },
                placeholder = { Text(text = stringResource(id = R.string.full_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            EmailTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailValueChange(it) },
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PasswordTextField(
                value = state.password,
                isVisible = state.isPasswordVisible,
                onValueChange = { viewModel.onPasswordValueChange(it) },
                onToggleVisibility = { viewModel.onTogglePasswordVisibility() },
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PasswordTextField(value = state.repeatedPassword,
                isVisible = state.isRepeatedPasswordVisible,
                onValueChange = { viewModel.onRepeatedPasswordValueChange(it) },
                onToggleVisibility = { viewModel.onToggleRepeatedPasswordVisibility() },
                label = { Text(text = stringResource(R.string.repeat_password)) },
                placeholder = { Text(text = stringResource(R.string.repeat_password)) })

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Checkbox(
                    checked = state.isAgreeTermsPrivacyPolicy,
                    onCheckedChange = { viewModel.onCheckedChangeTermsPrivacyPolicy() },
                )

                Text(text = stringResource(R.string.i_agree_to_the).plus(" "))

                Text(
                    text = stringResource(R.string.terms),
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.clickable { },
                )

                Text(text = " ${stringResource(R.string.and)} ")

                Text(
                    text = stringResource(R.string.privacy_policy),
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.clickable { },
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            GradientButton(
                enabled = !state.isLoading,
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        if (state.isLoading) R.string.registering_an_account
                        else R.string.register
                    ),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = stringResource(R.string.or).uppercase(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            OutlinedGradientButton(
                enabled = !state.isLoading,
                onClick = { googleSignInLauncher.launch(0) },
                modifier = Modifier.fillMaxWidth(),
            ) {

                AsyncImage(
                    model = R.drawable.ic_google,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                Text(
                    text = stringResource(
                        if (state.isLoadingGoogleLogin) R.string.logging_in
                        else R.string.continue_with_google
                    ),
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.already_have_an_account).plus(" "))
                Text(text = stringResource(R.string.login),
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navigator.popBackStack() })
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
        }
    }
}