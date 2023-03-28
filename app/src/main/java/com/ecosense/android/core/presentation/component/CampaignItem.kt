package com.ecosense.android.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.presentation.theme.ElectricOrange
import com.ecosense.android.core.presentation.theme.ElectricYellow
import com.ecosense.android.core.presentation.theme.White
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.data.util.campaignEndedStatus
import com.ecosense.android.featDiscoverCampaign.data.util.campaignNotStartedStatus
import com.ecosense.android.featDiscoverCampaign.data.util.dateTimeFormatter

@Composable
fun CampaignItem(
    campaign: Campaign,
    sort: String?,
    onClick: () -> Unit
) {
    if (sort == "" || sort == stringResource(R.string.show_all_campaign)) {
        ShowItem(
            campaign = campaign,
            onClick = onClick
        )
    } else if (sort == stringResource(R.string.new_campaign)) {
        if (campaign.isNew) {
            ShowItem(
                campaign = campaign,
                onClick = onClick
            )
        }
    } else if (sort == stringResource(R.string.trending_campaign)) {
        if (campaign.isTrending) {
            ShowItem(
                campaign = campaign,
                onClick = onClick
            )
        }
    } else if (sort == stringResource(R.string.on_going_campaign)) {
        if (!campaignEndedStatus(campaign.endDate) && !campaignNotStartedStatus(campaign.startDate)) {
            ShowItem(
                campaign = campaign,
                onClick = onClick
            )
        }
    } else if (sort == stringResource(R.string.coming_soon_campaign)) {
        if (campaignNotStartedStatus(campaign.startDate)) {
            ShowItem(
                campaign = campaign,
                onClick = onClick
            )
        }
    } else if (sort == stringResource(R.string.finished_campaign)) {
        if (campaignEndedStatus(campaign.endDate)) {
            ShowItem(
                campaign = campaign,
                onClick = onClick
            )
        }
    }
}

@Composable
fun ShowItem(
    campaign: Campaign,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium
            )
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .height(165.dp)
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.small)
                ) {
                    if (campaign.isTrending) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(10.dp))
                                .background(ElectricOrange)
                                .padding(
                                    horizontal = MaterialTheme.spacing.extraSmall,
                                    vertical = 1.dp
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Whatshot,
                                contentDescription = stringResource(R.string.trending_campaign),
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.height(10.dp)
                            )
                            Spacer(modifier = Modifier.width(1.dp))
                            Text(
                                text = stringResource(R.string.trending_label),
                                style = MaterialTheme.typography.overline,
                                color = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    if (campaign.isNew) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(10.dp))
                                .background(ElectricYellow)
                                .padding(
                                    horizontal = MaterialTheme.spacing.extraSmall,
                                    vertical = 1.dp
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.NewReleases,
                                contentDescription = stringResource(R.string.new_campaign),
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.height(10.dp)
                            )
                            Spacer(modifier = Modifier.width(1.dp))
                            Text(
                                text = stringResource(R.string.new_label),
                                style = MaterialTheme.typography.overline,
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = campaign.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(2.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.extraSmall)
            ) {
                items(campaign.categories.size) { i ->
                    Text(
                        text = campaign.categories[i].name,
                        style = MaterialTheme.typography.overline,
                        fontWeight = FontWeight.Medium,
                        color = White,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(Color(campaign.categories[i].colorHex.toColorInt()))
                            .padding(horizontal = MaterialTheme.spacing.small, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = R.drawable.ic_group_participant,
                    contentDescription = stringResource(R.string.participant_count),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                    modifier = Modifier.size(12.dp)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                Text(
                    text = campaign.participantsCount.toString(),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colors.secondary
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = stringResource(R.string.participated),
                    style = MaterialTheme.typography.caption
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            if (campaignNotStartedStatus(campaign.startDate)) {
                Text(
                    text = stringResource(R.string.campaign_will_start_at),
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = dateTimeFormatter(campaign.startDate, context),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
            } else {
                if (campaignEndedStatus(campaign.endDate)) {
                    Text(
                        text = stringResource(R.string.campaign_ended_at),
                        style = MaterialTheme.typography.caption
                    )
                } else {
                    Text(
                        text = stringResource(R.string.campaign_will_end_at),
                        style = MaterialTheme.typography.caption
                    )
                }
                Text(
                    text = dateTimeFormatter(campaign.endDate, context),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}