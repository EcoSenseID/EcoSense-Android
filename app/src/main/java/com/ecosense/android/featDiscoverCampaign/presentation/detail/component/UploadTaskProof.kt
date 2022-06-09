package com.ecosense.android.featDiscoverCampaign.presentation.detail.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun UploadTaskProof(viewModel: CampaignDetailViewModel, task: CampaignTask, campaignId: Int) {
    val state = viewModel.state.value
    val openPickImageDialog = remember { mutableStateOf(false) }

    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    if (openPickImageDialog.value) {
        AttachImageDialog(
            viewModel = viewModel,
            openPickImageDialog = { openPickImageDialog.value = it }
        )
    }

    Row(
        modifier = Modifier.padding(
            bottom = MaterialTheme.spacing.small
        )
    ) {
        Button(
            onClick = {
                openPickImageDialog.value = true
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
                imageVector = Icons.Outlined.AttachFile,
                contentDescription = stringResource(R.string.attach_proof_photo)
            )
            Spacer(Modifier.width(MaterialTheme.spacing.extraSmall))
            Text(stringResource(R.string.attach_proof_photo))
        }
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