package com.ecosense.android.featProfile.presentation.changePassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featAuth.presentation.component.PasswordTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun ChangePasswordScreen(
    navigator: DestinationsNavigator,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(context)
                )
                is UIEvent.Finish -> navigator.navigateUp()
                else -> {}
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        IconButton(onClick = { navigator.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(id = R.string.cd_back),
                                tint = MaterialTheme.colors.secondary,
                            )
                        }
                    }

                    Text(
                        text = stringResource(R.string.change_password),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(scaffoldPadding),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    PasswordTextField(
                        value = viewModel.currentPasswordValue,
                        isVisible = viewModel.currentPasswordVisibility,
                        onValueChange = { viewModel.onValueChangeCurrentPassword(it) },
                        onToggleVisibility = { viewModel.onToggleVisibilityCurrentPassword() },
                        label = { Text(text = stringResource(R.string.current_password)) },
                        placeholder = { Text(text = stringResource(R.string.current_password)) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    PasswordTextField(
                        value = viewModel.newPasswordValue,
                        isVisible = viewModel.newPasswordVisibility,
                        onValueChange = { viewModel.onValueChangeNewPassword(it) },
                        onToggleVisibility = { viewModel.onToggleVisibilityNewPassword() },
                        label = { Text(text = stringResource(R.string.new_password)) },
                        placeholder = { Text(text = stringResource(R.string.new_password)) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    PasswordTextField(
                        value = viewModel.repeatedNewPasswordValue,
                        isVisible = viewModel.repeatedNewPasswordVisibility,
                        onValueChange = { viewModel.onValueChangeRepeatedNewPassword(it) },
                        onToggleVisibility = { viewModel.onToggleVisibilityRepeatedNewPassword() },
                        label = { Text(text = stringResource(R.string.retype_new_password)) },
                        placeholder = { Text(text = stringResource(R.string.retype_new_password)) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    GradientButton(
                        onClick = { viewModel.onClickSubmit() },
                        enabled = !viewModel.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = if (viewModel.isLoading) stringResource(R.string.changing_password)
                            else stringResource(id = R.string.confirm_change),
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}