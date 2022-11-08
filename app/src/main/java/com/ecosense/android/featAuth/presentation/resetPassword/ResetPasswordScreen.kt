package com.ecosense.android.featAuth.presentation.resetPassword

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.ecosense.android.featAuth.presentation.component.AuthTopBar
import com.ecosense.android.featAuth.presentation.component.EmailTextField
import com.ecosense.android.featAuth.presentation.component.OutlinedGradientButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalUnitApi::class)
@Composable
@Destination
fun ResetPasswordScreen(
    navigator: DestinationsNavigator,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
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
                else -> {}
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState, modifier = Modifier.fillMaxSize()
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

            if (!state.isSuccessful) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    fontSize = TextUnit(34f, TextUnitType.Sp),
                    fontWeight = FontWeight.ExtraBold,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                Text(
                    text = stringResource(R.string.reset_password_screen_caption),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                EmailTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEmailValueChange(it) })

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                GradientButton(
                    enabled = !state.isLoading,
                    onClick = { viewModel.onSendInstructionClick() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            if (state.isLoading) R.string.forgot_password_submitting
                            else R.string.forgot_password_submit
                        ),
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                OutlinedGradientButton(
                    onClick = { navigator.navigateUp() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.back_to_login_page),
                        fontWeight = FontWeight.Bold,
                    )
                }
            } else {
                AsyncImage(
                    model = R.drawable.ic_mail,
                    contentDescription = null,
                    modifier = Modifier
                        .height(96.dp)
                        .fillMaxWidth(),
                )

                Text(
                    text = stringResource(R.string.check_your_mail),
                    fontSize = TextUnit(34f, TextUnitType.Sp),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                Text(
                    text = stringResource(R.string.check_your_email_caption),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                GradientButton(
                    onClick = {
                        Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_APP_EMAIL) }
                            .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                            .let { context.startActivity(it) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.open_email_app),
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                OutlinedGradientButton(
                    onClick = { navigator.navigateUp() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.skip_ill_confirm_later),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
        }
    }
}