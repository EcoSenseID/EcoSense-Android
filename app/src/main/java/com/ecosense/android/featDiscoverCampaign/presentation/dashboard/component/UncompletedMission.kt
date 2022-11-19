package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.DarkGrey
import com.ecosense.android.core.presentation.theme.MintGreen
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.CampaignDetailScreenDestination
import com.ecosense.android.featDiscoverCampaign.data.util.countDays
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.domain.model.DashboardCampaign
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun UncompletedMission(
    navigator: DestinationsNavigator,
    campaigns: DashboardCampaign,
    campaignSize: Int,
    modifier: Modifier = Modifier
) {
    val verticalScrollState = rememberScrollState()

    if (campaigns.uncompletedMissions.isNotEmpty()) {
        if (campaignSize == 1) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .padding(MaterialTheme.spacing.medium)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                    .clip(shape = RoundedCornerShape(20.dp))
                    .verticalScroll(verticalScrollState)
            ) {
                campaigns.uncompletedMissions.forEach { uncompletedMission ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .background(MaterialTheme.colors.surface)
                            .clickable { navigator.navigate(CampaignDetailScreenDestination(id = campaigns.id)) }
                            .padding(
                                top = MaterialTheme.spacing.small,
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
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
                                        text = uncompletedMission.name,
                                        style = MaterialTheme.typography.body1,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = campaigns.name,
                                        style = MaterialTheme.typography.caption,
                                        fontWeight = FontWeight.Bold,
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
                                                    countDays(campaigns.endDate)
                                                )
                                            } else
                                                "",
                                            style = MaterialTheme.typography.caption,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = stringResource(
                                                R.string.until_date,
                                                dateFormatter(campaigns.endDate)
                                            ),
                                            style = MaterialTheme.typography.caption,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Divider(
                                        color = DarkGrey,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .width(350.dp)
                    .height(225.dp)
                    .padding(MaterialTheme.spacing.medium)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                    .clip(shape = RoundedCornerShape(20.dp))
                    .verticalScroll(verticalScrollState)
            ) {
                campaigns.uncompletedMissions.forEach { uncompletedMission ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .background(MaterialTheme.colors.surface)
                            .clickable { navigator.navigate(CampaignDetailScreenDestination(id = campaigns.id)) }
                            .padding(
                                top = MaterialTheme.spacing.small,
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
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
                                        text = uncompletedMission.name,
                                        style = MaterialTheme.typography.body1,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = campaigns.name,
                                        style = MaterialTheme.typography.caption,
                                        fontWeight = FontWeight.Bold,
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
                                                    countDays(campaigns.endDate)
                                                )
                                            } else
                                                "",
                                            style = MaterialTheme.typography.caption,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = stringResource(
                                                R.string.until_date,
                                                dateFormatter(campaigns.endDate)
                                            ),
                                            style = MaterialTheme.typography.caption,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Divider(
                                        color = DarkGrey,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        if (campaignSize == 1) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .padding(MaterialTheme.spacing.medium)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                    .background(color = MintGreen)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .clickable { navigator.navigate(CampaignDetailScreenDestination(id = campaigns.id)) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_missions_left),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        AsyncImage(
                            model = R.drawable.character_07,
                            contentDescription = stringResource(R.string.ecobot),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .width(350.dp)
                    .height(225.dp)
                    .padding(MaterialTheme.spacing.medium)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                    .background(color = MintGreen)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .clickable { navigator.navigate(CampaignDetailScreenDestination(id = campaigns.id)) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_missions_left),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        AsyncImage(
                            model = R.drawable.character_07,
                            contentDescription = stringResource(R.string.ecobot),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}