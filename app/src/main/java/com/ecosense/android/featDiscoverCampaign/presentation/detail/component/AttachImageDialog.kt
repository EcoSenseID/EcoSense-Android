package com.ecosense.android.featDiscoverCampaign.presentation.detail.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.presentation.detail.CampaignDetailViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AttachImageDialog(viewModel: CampaignDetailViewModel, openPickImageDialog: (Boolean) -> Unit) {
    val state = viewModel.state.value
    val context = LocalContext.current

    val openGoToSettingsDialog = remember { mutableStateOf(false)  }

    val coroutineScope = rememberCoroutineScope()
    val camPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val cameraImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.onImageCaptured()
            openPickImageDialog(false)
        }
    }

    val galleryImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImagePicked(uri)
        openPickImageDialog(false)
    }

    Dialog(onDismissRequest = { openPickImageDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colors.surface
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.choose_image_from),
                            color = MaterialTheme.colors.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.h6
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(R.string.cancel),
                            modifier = Modifier
                                .clickable { openPickImageDialog(false) }
                                .size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.padding(
                            bottom = MaterialTheme.spacing.small
                        )
                    ) {
                        Button(
                            onClick = {
                                galleryImagePickerLauncher.launch(
                                    context.getString(R.string.content_type_image)
                                )
                            },
                            enabled = !state.isLoadingUploadProof,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    shape = RoundedCornerShape(
                                        20.dp
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

                    Row {
                        Button(
                            onClick = {
                                if (camPermission.hasPermission) {
                                    coroutineScope.launch {
                                        val uri = viewModel.getNewTempJpegUri()
                                        cameraImagePickerLauncher.launch(uri)
                                    }
                                }
                                else {
                                    when {
                                        !camPermission.permissionRequested -> camPermission.launchPermissionRequest()
                                        camPermission.shouldShowRationale -> camPermission.launchPermissionRequest()
                                        else -> openGoToSettingsDialog.value = true
                                    }
                                }
                            },
                            enabled = !state.isLoadingUploadProof,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    shape = RoundedCornerShape(
                                        20.dp
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

                    if (openGoToSettingsDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                openGoToSettingsDialog.value = false
                            },
                            title = { Text(text = stringResource(R.string.whoops)) },
                            text = { Text(stringResource(R.string.upload_permission_request_denied_permanently)) },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        openGoToSettingsDialog.value = false
                                        Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", context.packageName, null)
                                        ).apply {
                                            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            context.startActivity(this)
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(
                                            bottom = MaterialTheme.spacing.extraSmall,
                                            end = MaterialTheme.spacing.extraSmall
                                        )
                                        .clip(
                                            shape = RoundedCornerShape(
                                                20.dp
                                            )
                                        )
                                ) {
                                    Text(stringResource(R.string.open_app_settings))
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        openGoToSettingsDialog.value = false
                                    },
                                    modifier = Modifier
                                        .padding(bottom = MaterialTheme.spacing.extraSmall)
                                        .clip(
                                            shape = RoundedCornerShape(
                                                20.dp
                                            )
                                        )
                                ) {
                                    Text(stringResource(R.string.cancel))
                                }
                            },
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }
        }
    }
}