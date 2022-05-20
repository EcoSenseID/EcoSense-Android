package com.ecosense.android.featAuth.presentation.resetPassword

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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@AuthNavGraph
fun ResetPasswordScreen(
    navigator: DestinationsNavigator,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(text = stringResource(R.string.reset_password))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            EmailTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.onEmailValueChange(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Button(
                onClick = { viewModel.onSendInstructionClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.send_instruction))
            }
        }
    }
}