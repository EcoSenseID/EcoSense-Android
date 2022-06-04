package com.ecosense.android.core.presentation.component

import android.os.Build
import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter

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
    }
}

@Composable
fun ShowItem(
    campaign: Campaign,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .width(120.dp)
                .height(160.dp)
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
                                .background(Color.Red)
                                .padding(MaterialTheme.spacing.extraSmall)
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
                                .background(Color.Yellow)
                                .padding(MaterialTheme.spacing.extraSmall)
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

        Column(modifier = Modifier.fillMaxWidth()) {
//            Text(
//                text = stringResource(R.string.until_date,
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                        dateFormatter(campaign.endDate)
//                    else
//                        campaign.endDate),
//                style = MaterialTheme.typography.caption,
//                modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
//            )
            Text(
                text = campaign.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.extraSmall)
            ) {
                items(campaign.category.size) { i ->
                    Text(
                        text = campaign.category[i],
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(
                                if (campaign.category[i] == stringResource(R.string.cat_air_pollution)) {
                                    Color.Green
                                } else if (campaign.category[i] == stringResource(R.string.cat_food_waste)) {
                                    Color.Magenta
                                } else {
                                    MaterialTheme.colors.primary
                                }
                            )
                            .padding(MaterialTheme.spacing.extraSmall)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                }
            }

            Text(
                text = stringResource(R.string.change_maker, campaign.participantsCount),
                style = MaterialTheme.typography.caption
            )
        }
    }
}