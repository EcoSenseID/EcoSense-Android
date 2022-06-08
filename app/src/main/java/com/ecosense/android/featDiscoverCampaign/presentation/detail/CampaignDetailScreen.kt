package com.ecosense.android.featDiscoverCampaign.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ecosense.android.featDiscoverCampaign.presentation.detail.component.UploadTaskProof
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun CampaignDetailScreen(
    navigator: DestinationsNavigator,
    id: Int,
    viewModel: CampaignDetailViewModel = hiltViewModel()
) {
    remember { viewModel.setCampaignId(id = id) }

    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value
    val campaign = state.campaignDetail

    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            DiscoverTopBar(
                onBackClick = {
                    navigator.popBackStack()
                }
            )
        },
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (!campaign.joined && !state.isLoadingCampaignDetail) {
                if (!state.isLoadingJoinCampaign) {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(R.string.join_campaign),
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                        onClick = {
                            viewModel.onJoinCampaign(campaignId = id)
                        }
                    )
                } else {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(R.string.joining_campaign),
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        backgroundColor = Color.Gray,
                        onClick = {}
                    )
                }
            }
        }
    ) {
        if (state.isLoadingCampaignDetail) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.wrapContentSize(),
                    strokeWidth = 3.dp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small
                        )
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                        .clip(shape = RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(campaign.posterUrl)
                                    .crossfade(true)
                                    .scale(Scale.FILL)
                                    .build(),
                                contentDescription = stringResource(
                                    R.string.poster_of_campaign,
                                    campaign.title
                                ),
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxSize()
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                MaterialTheme.colors.primary
                                            )
                                        )
                                    )
                                    .padding(MaterialTheme.spacing.small),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = stringResource(R.string.participant_count),
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = campaign.participantsCount.toString(),
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                }
                                Row {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = MaterialTheme.spacing.extraSmall)
                                    ) {
                                        for (campaignCategory in campaign.category) {
                                            Text(
                                                text = campaignCategory,
                                                style = MaterialTheme.typography.overline,
                                                color = Color.White,
                                                modifier = Modifier
                                                    .clip(shape = RoundedCornerShape(10.dp))
                                                    .background(
                                                        when (campaignCategory) {
                                                            stringResource(R.string.cat_water_pollution) -> {
                                                                Color("#206A5D".toColorInt())
                                                            }
                                                            stringResource(R.string.cat_air_pollution) -> {
                                                                Color("#81B214".toColorInt())
                                                            }
                                                            stringResource(R.string.cat_food_waste) -> {
                                                                Color("#F58634".toColorInt())
                                                            }
                                                            stringResource(R.string.cat_plastic_free) -> {
                                                                Color("#E25DD7".toColorInt())
                                                            }
                                                            stringResource(R.string.cat_energy_efficiency) -> {
                                                                Color("#DB3069".toColorInt())
                                                            }
                                                            else -> {
                                                                MaterialTheme.colors.primary
                                                            }
                                                        }
                                                    )
                                                    .padding(
                                                        horizontal = MaterialTheme.spacing.extraSmall,
                                                        vertical = 1.dp
                                                    )
                                            )
                                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                                        }
                                    }
                                }
                                Row {
                                    Text(
                                        text = campaign.title,
                                        style = MaterialTheme.typography.h5,
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
                        .fillMaxWidth()
                        .padding(
                            top = MaterialTheme.spacing.small,
                            bottom = MaterialTheme.spacing.medium,
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.medium
                        )
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(MaterialTheme.spacing.small)
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
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = stringResource(R.string.initiator),
                                        tint = MaterialTheme.colors.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = stringResource(R.string.initiated_by),
                                        style = MaterialTheme.typography.caption
                                    )
                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                                    Text(
                                        text = campaign.initiator,
                                        style = MaterialTheme.typography.caption,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.primary
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(MaterialTheme.spacing.small)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row {
                                            Text(
                                                text = dateFormatter(campaign.startDate),
                                                style = MaterialTheme.typography.subtitle1,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colors.primary
                                            )
                                        }
                                        Row {
                                            Text(
                                                text = stringResource(R.string.start_date),
                                                style = MaterialTheme.typography.body2,
                                                color = MaterialTheme.colors.primary
                                            )
                                        }
                                    }

                                    Column {
                                        Divider(
                                            color = MaterialTheme.colors.primary,
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(1.dp)
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row {
                                            Text(
                                                text = dateFormatter(campaign.endDate),
                                                style = MaterialTheme.typography.subtitle1,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colors.primary
                                            )
                                        }
                                        Row {
                                            Text(
                                                text = stringResource(R.string.end_date),
                                                style = MaterialTheme.typography.body2,
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
                                                text = stringResource(R.string.impact),
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
                                                style = MaterialTheme.typography.caption
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
                                                text = stringResource(R.string.tasks),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .padding(bottom = MaterialTheme.spacing.medium)
                                            )
                                        }
                                        Row(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)) {
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                campaign.campaignTasks.forEachIndexed { index, task ->
                                                    Row(
                                                        verticalAlignment = Alignment.Top,
                                                        modifier = Modifier.padding(
                                                            bottom = MaterialTheme.spacing.small
                                                        )
                                                    ) {
                                                        Column {
                                                            Text(
                                                                text = "${index + 1}",
                                                                fontSize = 18.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = MaterialTheme.colors.primary,
                                                                modifier = Modifier
                                                                    .padding(end = MaterialTheme.spacing.medium)
                                                            )
                                                        }
                                                        Column {
                                                            Text(
                                                                text = task.name,
                                                                textAlign = TextAlign.Justify,
                                                                style = MaterialTheme.typography.body1,
                                                                modifier = Modifier
                                                                    .padding(top = 2.dp)
                                                            )
                                                        }
                                                    }
                                                    if (campaign.joined) {
                                                        if (task.completed) {
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .wrapContentHeight()
                                                                    .padding(bottom = MaterialTheme.spacing.small)
                                                            ) {
                                                                AsyncImage(
                                                                    model = ImageRequest.Builder(context)
                                                                        .data(task.proofPhotoUrl)
                                                                        .crossfade(true)
                                                                        .scale(Scale.FILL)
                                                                        .build(),
                                                                    contentDescription = stringResource(
                                                                        R.string.proof_completion,
                                                                        task.name
                                                                    ),
                                                                    contentScale = ContentScale.FillWidth,
                                                                    modifier = Modifier
                                                                        .fillMaxSize()
                                                                        .clip(
                                                                            shape = RoundedCornerShape(
                                                                                8.dp
                                                                            )
                                                                        )
                                                                )
                                                            }
                                                            Row(
                                                                modifier = Modifier.padding(
                                                                    bottom = MaterialTheme.spacing.small
                                                                )
                                                            ) {
                                                                Text(
                                                                    text = "\"${task.proofCaption}\"",
                                                                    fontStyle = FontStyle.Italic,
                                                                    style = MaterialTheme.typography.caption
                                                                )
                                                            }
                                                            Row {
                                                                Text(
                                                                    text = stringResource(
                                                                        R.string.finished_on,
                                                                        dateFormatter(task.completedTimeStamp)
                                                                    ),
                                                                    style = MaterialTheme.typography.caption,
                                                                    color = MaterialTheme.colors.primary
                                                                )
                                                            }
                                                        } else {
                                                            if (index == 0) {
                                                                UploadTaskProof(
                                                                    viewModel = viewModel,
                                                                    task = task,
                                                                    campaignId = id
                                                                )
                                                            } else if (index != 0 && campaign.campaignTasks[index - 1].completed) {
                                                                UploadTaskProof(
                                                                    viewModel = viewModel,
                                                                    task = task,
                                                                    campaignId = id
                                                                )
                                                            }
                                                        }
                                                    }
                                                    Row(
                                                        modifier = Modifier.padding(
                                                            vertical = MaterialTheme.spacing.small
                                                        )
                                                    ) {
                                                        Divider(
                                                            color = Color.Gray,
                                                            thickness = 1.dp,
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
            }
        }
    }
}