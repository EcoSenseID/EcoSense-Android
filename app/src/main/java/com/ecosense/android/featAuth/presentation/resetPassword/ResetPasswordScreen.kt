package com.ecosense.android.featAuth.presentation.resetPassword

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.AuthNavGraph
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featAuth.presentation.component.EmailTextField
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
@AuthNavGraph
fun ResetPasswordScreen(
    navigator: DestinationsNavigator,
    viewModel: ResetPasswordViewModel = hiltViewModel()
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
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large)
                .fillMaxSize()
        ) {

            Text(
                text = stringResource(R.string.reset_password_screen_title),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Text(text = stringResource(R.string.reset_password_screen_caption))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            EmailTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailValueChange(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            RoundedEndsButton(
                enabled = !state.isLoading,
                onClick = { viewModel.onSendInstructionClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        if (state.isLoading) R.string.sending_instruction
                        else R.string.send_instruction
                    )
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            if (state.isSuccessful) Text(
                text = stringResource(R.string.sm_reset_password),
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(MaterialTheme.spacing.medium)
            )
        }
    }
}