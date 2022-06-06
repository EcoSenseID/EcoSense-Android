package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.CampaignDetailScreenDestination
import com.ecosense.android.featDiscoverCampaign.data.util.countDays
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.domain.model.DashboardTask
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun OnGoingTasks(
    navigator: DestinationsNavigator,
    tasks: List<DashboardTask>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(bottom = MaterialTheme.spacing.medium)
    ) {
        items(tasks.size) { i ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(
                        vertical = MaterialTheme.spacing.small
                    )
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(
                        vertical = MaterialTheme.spacing.small,
                        horizontal = MaterialTheme.spacing.medium
                    )
                    .fillMaxWidth()
                    .clickable { navigator.navigate(CampaignDetailScreenDestination(id = tasks[i].campaignId)) }
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
                                text = tasks[i].name,
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.primary
                            )
                            Text(
                                text = tasks[i].campaignName,
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall)
                            )
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        stringResource(
                                            R.string.days_left,
                                            countDays(tasks[i].campaignEndDate)
                                        )
                                    } else
                                        "",
                                    style = MaterialTheme.typography.caption
                                )
                                Text(
                                    text = stringResource(R.string.until_date, dateFormatter(tasks[i].campaignEndDate)),
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