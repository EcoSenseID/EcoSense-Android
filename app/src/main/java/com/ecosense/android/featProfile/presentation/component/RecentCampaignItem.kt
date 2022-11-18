package com.ecosense.android.featProfile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.domain.constants.CampaignCompletionStatus
import com.ecosense.android.core.presentation.modifier.brushForeground
import com.ecosense.android.core.presentation.theme.Gradient
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.colorFromHex
import com.ecosense.android.featProfile.presentation.model.RecentCampaignPresentation

@Composable
fun RecentCampaignItem(
    campaign: () -> RecentCampaignPresentation,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        AsyncImage(
            model = campaign().posterUrl,
            fallback = painterResource(id = R.drawable.error_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(96.dp)
                .height(192.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.spacing.medium),
        ) {
            Text(
                text = campaign().title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .brushForeground(Gradient)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(count = campaign().categories.size) { i ->
                    Text(
                        text = campaign().categories[i].name,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .background(colorFromHex(hex = campaign().categories[i].colorHex))
                            .padding(horizontal = MaterialTheme.spacing.small),
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            if (campaign().completionStatus == CampaignCompletionStatus.FINISHED) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AsyncImage(
                        model = R.drawable.ic_ecosense_logo_vector,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                        modifier = Modifier
                            .size(12.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.primary,
                                shape = CircleShape,
                            )
                            .padding(2.dp),
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                    Text(
                        text = campaign().earnedPoints.toString(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                    Text(
                        text = stringResource(R.string.ecopoints),
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            when (campaign().completionStatus) {
                CampaignCompletionStatus.UNFINISHED -> {
                    Text(text = stringResource(R.string.campaign_will_end_at))
                    Text(
                        text = campaign().endAt,
                        fontWeight = FontWeight.Bold,
                    )
                }

                CampaignCompletionStatus.BEING_VERIFIED -> {
                    Text(text = stringResource(R.string.completion_is_being_verified))
                }

                CampaignCompletionStatus.FINISHED -> {
                    Text(text = stringResource(R.string.campaign_finished_at))
                    
                    Text(
                        text = campaign().finishedAt,
                        fontWeight = FontWeight.Bold,
                    )
                }

                CampaignCompletionStatus.ENDED -> {
                    Text(text = stringResource(R.string.campaign_ended_at))
                    Text(
                        text = campaign().endAt,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}