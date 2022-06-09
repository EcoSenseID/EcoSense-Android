package com.ecosense.android.featDiscoverCampaign.presentation.detail.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignTask
import com.ecosense.android.featDiscoverCampaign.presentation.detail.CampaignDetailViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadTaskProof(viewModel: CampaignDetailViewModel, task: CampaignTask, campaignId: Int) {
    val state = viewModel.state.value
    val openDialog = remember { mutableStateOf(false)  }
    val coroutineScope = rememberCoroutineScope()
    val camPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val cameraImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) viewModel.onImageCaptured()
    }

    val galleryImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.onImagePicked(uri) }

    val context = LocalContext.current

    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier.padding(
            bottom = MaterialTheme.spacing.extraSmall
        )
    ) {
        Button(
            onClick = {
                galleryImagePickerLauncher.launch(
                    context.getString(R.string.content_type_image)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        8.dp
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = stringResource(R.string.open_gallery)
            )
            Spacer(Modifier.width(MaterialTheme.spacing.extraSmall))
            Text(stringResource(R.string.open_gallery))
        }
    }

    Row(
        modifier = Modifier.padding(
            bottom = MaterialTheme.spacing.small
        )
    ) {
        Button(
            onClick = {
                when {
                    camPermission.hasPermission -> {
                        coroutineScope.launch {
                            val uri = viewModel.getNewTempJpegUri()
                            cameraImagePickerLauncher.launch(uri)
                        }
                    }
                    else -> {
                        when {
                            !camPermission.permissionRequested -> camPermission.launchPermissionRequest()
                            camPermission.shouldShowRationale -> camPermission.launchPermissionRequest()
                            else -> openDialog.value = true
                        }
                    }
                }
            },
            enabled = !state.isLoadingUploadProof,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        8.dp
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Outlined.PhotoCamera,
                contentDescription = stringResource(R.string.open_camera)
            )
            Spacer(Modifier.width(MaterialTheme.spacing.extraSmall))
            Text(stringResource(R.string.open_camera))
        }
    }

    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Whoops!") },
            text = { Text(stringResource(R.string.upload_permission_request_denied_permanently)) },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        ).apply {
                            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(this)
                        }
                    }
                ) {
                    Text(stringResource(R.string.open_app_settings))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (state.proofPhotoUrl != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = MaterialTheme.spacing.small)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(
                    LocalContext.current
                )
                    .data(state.proofPhotoUrl)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentScale = ContentScale.FillWidth,
                contentDescription = stringResource(
                    R.string.unsubmitted_proof,
                    task.name
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        shape = RoundedCornerShape(
                            8.dp
                        )
                    )
            )
        }
    }

    Row(
        modifier = Modifier.padding(
            bottom = MaterialTheme.spacing.small
        )
    ) {
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = {
                inputValue.value =
                    it
            },
            enabled = !state.isLoadingUploadProof,
            label = {
                Text(
                    stringResource(R.string.add_caption)
                )
            },
            modifier = Modifier.fillMaxSize(),
            textStyle = MaterialTheme.typography.caption
        )
    }
    Row(
        modifier = Modifier.padding(
            bottom = MaterialTheme.spacing.small
        )
    ) {
        Button(
            onClick = {
                viewModel.onUploadCompletionProof(
                    caption = inputValue.value.text,
                    taskId = task.id,
                    campaignId = campaignId
                )
            },
            enabled = !state.isLoadingUploadProof,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        8.dp
                    )
                )
        ) {
            if (!state.isLoadingUploadProof)
                Text(stringResource(R.string.submit))
            else
                Text(stringResource(R.string.submitting))
        }
    }
}