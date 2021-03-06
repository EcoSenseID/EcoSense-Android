package com.ecosense.android.featProfile.presentation.editProfile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
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

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.onImagePicked(uri) }

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
                onBackClick = { navigator.navigateUp() },
            )
        },
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) { scaffoldPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPadding)
                .padding(MaterialTheme.spacing.medium)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true,
                )
                .background(color = MaterialTheme.colors.surface)
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.photoUrl)
                        .placeholder(R.drawable.ic_ecosense_logo)
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

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            imagePickerLauncher.launch(
                                context.getString(R.string.content_type_image)
                            )
                        }
                        .padding(MaterialTheme.spacing.small)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                        text = stringResource(R.string.change_profile_picture),
                        style = MaterialTheme.typography.subtitle2.copy(
                            textDecoration = TextDecoration.Underline,
                        ),
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.SemiBold,
                    )
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
                trailingIcon = {
                    Icon(
                        imageVector =
                        if (state.isEmailVerified == true) Icons.Default.Check
                        else Icons.Default.Warning,
                        contentDescription = stringResource(
                            if (state.isEmailVerified == true) R.string.email_is_verified
                            else R.string.email_is_unverified
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            if (state.isEmailVerified == false) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.email_is_not_verified).plus(" "))
                    Text(
                        text = stringResource(
                            if (state.isEmailVerificationLoading) R.string.sending
                            else R.string.send_email_verification
                        ),
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { viewModel.onSendEmailVerificationClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Button(onClick = { viewModel.onSaveClick() }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}