package com.ecosense.android.featDiscoverCampaign.presentation.detail.component

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
    val coroutineScope = rememberCoroutineScope()
    val camPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
//            uri: Uri? -> viewModel.onImagePicked(uri)
        if (success) viewModel.onImageCaptured()
    }

    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier.padding(
            bottom = MaterialTheme.spacing.small
        )
    ) {
        Button(
            onClick = {
                // TODO: currently can only pick image from gallery, find a way to make it able to use camera x too
//                imagePickerLauncher.launch(
//                    context.getString(R.string.content_type_image)
//                )
                when {
                    camPermission.hasPermission -> {
                        coroutineScope.launch {
                            val uri = viewModel.getNewTempJpegUri()
                            imagePickerLauncher.launch(uri)
                        }
                    }
                    else -> camPermission.launchPermissionRequest()
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        8.dp
                    )
                )
        ) { Text(stringResource(R.string.select_image)) }
    }

    if (state.proofPhotoUrl != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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
                    caption = inputValue.value.toString(),
                    taskId = task.id,
                    campaignId = campaignId
                )
                //TODO: trigger the compose to reload the page
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        8.dp
                    )
                )
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}