package com.ecosense.android.featProfile.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.CategoryColors
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featProfile.presentation.profile.model.FinishedCampaignPresentation

@Composable
fun CampaignHistoryItem(
    campaign: () -> FinishedCampaignPresentation,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        AsyncImage(
            model = campaign().posterUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(96.dp)
                .fillMaxHeight(),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(text = campaign().title)
            Text(stringResource(R.string.completed_at))
            Text(text = campaign().completedAt)
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                item {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                }

                items(count = campaign().categories.size) { i ->
                    Text(
                        text = campaign().categories[i],
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .background(CategoryColors[i % CategoryColors.size])
                            .padding(horizontal = MaterialTheme.spacing.small),
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                }
            }
        }
    }
}