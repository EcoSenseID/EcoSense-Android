package com.ecosense.android.featDiscoverCampaign.presentation.detail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
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
        OutlinedButton(
            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                openPickImageDialog.value = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = R.drawable.ic_attachment,
                    contentDescription = stringResource(R.string.attach_proof_photo),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(
                    text = stringResource(R.string.attach_proof_photo),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

    if (state.proofPhotoUrl != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
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
                            20.dp
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
            shape = RoundedCornerShape(20.dp),
            label = {
                Text(
                    stringResource(R.string.write_your_experience)
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
        GradientButton(
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
                        20.dp
                    )
                )
        ) {
            if (!state.isLoadingUploadProof)
                Text(
                    text = stringResource(R.string.submit),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button
                )
            else
                Text(
                    text = stringResource(R.string.submitting),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button
                )

        }
    }
}