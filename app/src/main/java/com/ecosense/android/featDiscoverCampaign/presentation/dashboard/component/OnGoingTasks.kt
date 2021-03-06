package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .padding(horizontal = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(),
                strokeWidth = 3.dp
            )
        }
    } else {
        if (tasks.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .padding(horizontal = MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = R.drawable.ic_wind_turbine_pana,
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Text(text = stringResource(R.string.empty_on_going_tasks), textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                items(tasks.size) { i ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .padding(
                                bottom = MaterialTheme.spacing.small
                            )
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colors.surface)
                            .clickable { navigator.navigate(CampaignDetailScreenDestination(id = tasks[i].campaignId)) }
                            .padding(
                                vertical = MaterialTheme.spacing.small,
                                horizontal = MaterialTheme.spacing.medium
                            )
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
                                            text = stringResource(
                                                R.string.until_date,
                                                dateFormatter(tasks[i].campaignEndDate)
                                            ),
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
}