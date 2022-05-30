package com.ecosense.android.featProfile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.presentation.theme.spacing

@Composable
fun HistoryCampaignItem(
    campaign: Campaign,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium
            )
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colors.surface)
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .width(64.dp)
                .fillMaxHeight()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(campaign.posterUrl)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = campaign.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
                    if (campaign.isNew) Text(
                        text = "New",
                        color = MaterialTheme.colors.onPrimary
                    )
                    if (campaign.isTrending) Text(
                        text = "Trending",
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = campaign.endDate,
                style = MaterialTheme.typography.caption
            )
            Text(
                text = campaign.title,
                fontWeight = FontWeight.Bold
            )

            Text(text = campaign.category.joinToString())

            Text(text = campaign.participantsCount.toString())
        }
    }
}