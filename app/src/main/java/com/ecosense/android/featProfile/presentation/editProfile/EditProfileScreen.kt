package com.ecosense.android.featProfile.presentation.editProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featAuth.presentation.component.AuthTextField
import com.ecosense.android.featAuth.presentation.component.EmailTextField
import com.ecosense.android.featProfile.presentation.component.EditProfileTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun EditProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: EditProfileViewModel = hiltViewModel()
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
        topBar = {
            EditProfileTopBar(
                isSavingLoading = state.isSavingProfileLoading,
                onBackClick = { navigator.popBackStack() },
                onSaveClick = { viewModel.onSaveClick() }
            )
        },
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(MaterialTheme.spacing.medium)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.photoUrl)
                        .error(R.drawable.ic_ecosense_logo)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = stringResource(R.string.cd_profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                Button(onClick = { viewModel.onChangeProfilePictureClick() }) {
                    Text(text = stringResource(R.string.change_profile_picture))
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            AuthTextField(
                value = state.displayName ?: "",
                onValueChange = { viewModel.onDisplayNameChange(it) },
                label = { Text(text = stringResource(R.string.name)) },
                placeholder = { Text(text = stringResource(R.string.name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            EmailTextField(
                value = state.email ?: "",
                enabled = false,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            if (state.isEmailVerified == false) {
                Text(text = stringResource(R.string.email_is_not_verified))
                Button(
                    enabled = !state.isEmailVerificationLoading,
                    onClick = { viewModel.onSendEmailVerificationClick() }
                ) {
                    Text(
                        text = stringResource(
                            if (state.isEmailVerificationLoading) R.string.sending
                            else R.string.send_email_verification
                        )
                    )
                }
            }
        }
    }
}