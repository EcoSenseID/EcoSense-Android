package com.ecosense.android.featDiscoverCampaign.presentation.detail

import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.core.presentation.component.CampaignItem
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.destinations.DetailCampaignScreenDestination
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun DetailCampaignScreen(
    navigator: DestinationsNavigator,
    id: Int,
    viewModel: DetailCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
               Text("Detail Campaign")
            }

            for (campaign in viewModel.campaignDetailList.value) {
                if (campaign.id == id) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            )
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                           Box(modifier = Modifier.fillMaxSize()) {
                               AsyncImage(
                                   model = ImageRequest.Builder(LocalContext.current)
                                       .data(campaign.posterUrl)
                                       .crossfade(true)
                                       .scale(Scale.FILL)
                                       .build(),
                                   contentDescription = "Poster of {$campaign.title} campaign.",
                                   contentScale = ContentScale.FillWidth,
                                   modifier = Modifier
                                       .fillMaxSize()
                                       .clip(shape = RoundedCornerShape(8.dp))
                               )
                               Column(
                                   modifier = Modifier
                                       .fillMaxSize()
                                       .padding(MaterialTheme.spacing.small),
                                   verticalArrangement = Arrangement.Bottom,
                                   horizontalAlignment = Alignment.Start
                               ) {
                                   Row {
                                       Text(
                                           text = campaign.participantsCount.toString(),
                                           fontSize = 14.sp,
                                           color = MaterialTheme.colors.onPrimary
                                       )
                                   }
                                   Row {
                                       LazyRow(modifier = Modifier.fillMaxWidth()) {
                                           items(campaign.category.size) { i ->
                                               Text(
                                                   text = campaign.category[i],
                                                   fontSize = 12.sp,
                                                   color = MaterialTheme.colors.onPrimary,
                                                   modifier = Modifier
                                                       .padding(end = MaterialTheme.spacing.extraSmall)
                                                       .background(
                                                           if (campaign.category[i] == "#AirPollution") {
                                                               Color.Green
                                                           } else if (campaign.category[i] == "#FoodWaste") {
                                                               Color.Magenta
                                                           } else {
                                                               MaterialTheme.colors.primary
                                                           }
                                                       )
                                               )
                                           }
                                       }
                                   }
                                   Row {
                                       Text(
                                           text = campaign.title,
                                           fontSize = 24.sp,
                                           fontWeight = FontWeight.Bold,
                                           color = MaterialTheme.colors.onPrimary
                                       )
                                   }

                               }
                           }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                top = MaterialTheme.spacing.small,
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
                            )
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colors.surface)
                            .padding(MaterialTheme.spacing.small)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(MaterialTheme.spacing.small),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column {
                                            Text(
                                                text = "Initiated by ",
                                                fontSize = 12.sp
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = campaign.initiator,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colors.primary
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .padding(MaterialTheme.spacing.small)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row {
                                                Text(
                                                    text =
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                                        dateFormatter(campaign.startDate)
                                                    else
                                                        campaign.startDate,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colors.primary
                                                )
                                            }
                                            Row {
                                                Text(
                                                    text = "Start Date",
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colors.primary
                                                )
                                            }
                                        }

                                        Column {
                                            Divider(
                                                color = MaterialTheme.colors.primary,
                                                modifier = Modifier
                                                    .height(50.dp)
                                                    .width(1.dp)
                                            )
                                        }

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row {
                                                Text(
                                                    text =
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                                        dateFormatter(campaign.endDate)
                                                    else
                                                        campaign.endDate,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colors.primary
                                                )
                                            }
                                            Row {
                                                Text(
                                                    text = "End Date",
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colors.primary
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .padding(MaterialTheme.spacing.small),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column {
                                            Row {
                                                Text(
                                                    text = "Impact",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .padding(bottom = MaterialTheme.spacing.small)
                                                )
                                            }
                                            Row {
                                                Text(
                                                    text = campaign.description,
                                                    textAlign = TextAlign.Justify,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .padding(MaterialTheme.spacing.small),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column {
                                            Row {
                                                Text(
                                                    text = "Tasks",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .padding(bottom = MaterialTheme.spacing.small)
                                                )
                                            }
                                            Row {
                                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                                    items(campaign.tasks.size) { i ->
                                                        val task = campaign.tasks[i]
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier
                                                                .padding(
                                                                    vertical = MaterialTheme.spacing.small,
                                                                    horizontal = MaterialTheme.spacing.medium
                                                                )
                                                        ) {
                                                            Column {
                                                                Text(
                                                                    text = "${i+1}",
                                                                    fontSize = 18.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = MaterialTheme.colors.primary,
                                                                    modifier = Modifier
                                                                        .padding(end = MaterialTheme.spacing.small)
                                                                )
                                                            }
                                                            Column {
                                                                Text(
                                                                    text = campaign.tasks[i].name,
                                                                    textAlign = TextAlign.Justify,
                                                                    fontSize = 12.sp
                                                                )
                                                            }
                                                        }
                                                        Row {
                                                            Divider(color = Color.Gray, thickness = 1.dp)
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
                }
                else {
                    Log.d("TAG", "DetailCampaignScreen: Detail not Found")
                }
            }
        }
    }
}