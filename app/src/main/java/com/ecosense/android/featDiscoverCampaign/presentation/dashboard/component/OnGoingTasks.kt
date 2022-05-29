package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.data.util.countDays
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail

@Composable
fun OnGoingTasks(
    campaign: CampaignDetail,
    modifier: Modifier = Modifier
) {
    if (campaign.isJoined) {
        campaign.tasks.forEachIndexed { index, _ ->
            if (!campaign.tasks[index].completed) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(
                            vertical = MaterialTheme.spacing.small
                        )
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = campaign.tasks[index].name,
                                    style = MaterialTheme.typography.h6
                                )
                                Text(
                                    text = campaign.title,
                                    style = MaterialTheme.typography.subtitle2,
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall)
                                )
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text =
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                                countDays(campaign.endDate) + " days left"
                                            else 
                                                "",
                                        style = MaterialTheme.typography.caption
                                    )
                                    Text(
                                        text = "Until ${
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                                dateFormatter(campaign.endDate)
                                            else
                                                campaign.endDate
                                        }",
                                        style = MaterialTheme.typography.caption
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}