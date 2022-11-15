package com.ecosense.android.featDiscoverCampaign.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.*
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.data.util.unixCountdown
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ecosense.android.featDiscoverCampaign.presentation.detail.component.UploadTaskProof
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun CampaignDetailScreen(
    navigator: DestinationsNavigator,
    id: Int,
    viewModel: CampaignDetailViewModel = hiltViewModel()
) {
    remember { viewModel.setCampaignId(id = id) }

    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val state = viewModel.state.value
    val campaign = state.campaignDetail

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // call the function every second to update the countdown
    var countdown by remember { mutableStateOf("") }

    if (!state.isLoadingCampaignDetail) {
        LaunchedEffect(Unit) {
            while (true) {
                countdown = unixCountdown(campaign.endDate)
                delay(1000)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    focusManager.clearFocus()
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }

                is UIEvent.HideKeyboard -> {
                    focusManager.clearFocus()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DiscoverTopBar(
                onBackClick = {
                    navigator.popBackStack()
                },
                screenName = stringResource(R.string.detail_campaign)
            )
        },
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (!campaign.joined && !state.isLoadingCampaignDetail) {
                if (!state.isLoadingJoinCampaign) {
                    GradientButton(
                        onClick = { viewModel.onJoinCampaign(campaignId = id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                    ) {
                        Text(
                            text = stringResource(R.string.join_campaign),
                            style = MaterialTheme.typography.body1,
                            color = White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(R.string.joining_campaign),
                                style = MaterialTheme.typography.body1,
                                color = White,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        backgroundColor = DarkGrey,
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
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
                        .padding(MaterialTheme.spacing.medium)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                        .clip(shape = RoundedCornerShape(20.dp))
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
                                    AsyncImage(
                                        model = R.drawable.ic_group_participant,
                                        contentDescription = stringResource(R.string.participant_count),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                                    Text(
                                        text = campaign.participantsCount.toString(),
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
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
                                                color = White,
                                                modifier = Modifier
                                                    .clip(shape = RoundedCornerShape(20.dp))
                                                    .background(
                                                        when (campaignCategory) {
                                                            stringResource(R.string.cat_water_pollution) -> {
                                                                DarkBlue
                                                            }
                                                            stringResource(R.string.cat_air_pollution) -> {
                                                                LightBlue
                                                            }
                                                            stringResource(R.string.cat_waste_free) -> {
                                                                CustardYellow
                                                            }
                                                            stringResource(R.string.cat_plastic_free) -> {
                                                                DarkRed
                                                            }
                                                            else -> {
                                                                MaterialTheme.colors.primary
                                                            }
                                                        }
                                                    )
                                                    .padding(
                                                        horizontal = MaterialTheme.spacing.small,
                                                        vertical = 2.dp
                                                    )
                                            )
                                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                                Row {
                                    Text(
                                        text = campaign.title,
                                        style = MaterialTheme.typography.h5,
                                        fontWeight = FontWeight.ExtraBold,
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
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                        .clip(shape = RoundedCornerShape(20.dp))
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
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.spacing.small),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = R.drawable.ic_initiator,
                                        contentDescription = stringResource(R.string.initiator),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                                        modifier = Modifier.size(16.dp)
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
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.spacing.small)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(20.dp))
                                            .shadow(
                                                elevation = 2.dp,
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colors.primary)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.campaign_will_end_in),
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colors.onPrimary,
                                                style = MaterialTheme.typography.caption,
                                                modifier = Modifier.padding(MaterialTheme.spacing.extraSmall)
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MintGreen)
                                        ) {
                                            Text(
                                                text = countdown,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colors.primary,
                                                style = MaterialTheme.typography.h6,
                                                modifier = Modifier.padding(MaterialTheme.spacing.small)
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
                                                text = stringResource(R.string.about_this_campaign),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                style = TextStyle(textDecoration = TextDecoration.Underline),
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
                                                text = stringResource(R.string.missions),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                style = TextStyle(textDecoration = TextDecoration.Underline),
                                                modifier = Modifier
                                                    .padding(bottom = MaterialTheme.spacing.small)
                                            )
                                        }
                                        Row {
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                campaign.campaignTasks.forEachIndexed { index, task ->
                                                    if (campaign.joined) {
                                                        Row(
                                                            modifier = Modifier.padding(
                                                                bottom = MaterialTheme.spacing.extraSmall
                                                            ),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Filled.CheckCircle,
                                                                tint =
                                                                if (task.completed)
                                                                    MaterialTheme.colors.secondary
                                                                else
                                                                    Color.Gray,
                                                                contentDescription = stringResource(
                                                                    R.string.mission_completion_mark
                                                                ),
                                                                modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                                                            )
                                                            Text(
                                                                text = stringResource(
                                                                    R.string.mission_number,
                                                                    index + 1
                                                                ),
                                                                style = MaterialTheme.typography.body1,
                                                                fontWeight = FontWeight.Bold,
                                                                color = MaterialTheme.colors.secondary,
                                                                modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall)
                                                            )
                                                        }
                                                        Row(
                                                            modifier = Modifier.padding(
                                                                bottom = MaterialTheme.spacing.small
                                                            )
                                                        ) {
                                                            Text(
                                                                text = task.name,
                                                                textAlign = TextAlign.Start,
                                                                style = MaterialTheme.typography.caption
                                                            )
                                                        }

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
                                                                                20.dp
                                                                            )
                                                                        )
                                                                )
                                                            }
                                                            Row(
                                                                modifier = Modifier.padding(
                                                                    bottom = MaterialTheme.spacing.small
                                                                )
                                                            ) {
                                                                if (task.proofCaption != "") {
                                                                    Text(
                                                                        text = "\"${task.proofCaption}\"",
                                                                        fontWeight = FontWeight.Bold,
                                                                        style = MaterialTheme.typography.caption
                                                                    )
                                                                }
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
                                                    } else {
                                                        Row(
                                                            verticalAlignment = Alignment.Top,
                                                            modifier = Modifier.padding(
                                                                MaterialTheme.spacing.extraSmall
                                                            )
                                                        ) {
                                                            Column {
                                                                Text(
                                                                    text = "${index + 1}.",
                                                                    style = MaterialTheme.typography.caption,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = MaterialTheme.colors.secondary
                                                                )
                                                            }
                                                            Spacer(
                                                                modifier = Modifier.width(
                                                                    MaterialTheme.spacing.small
                                                                )
                                                            )
                                                            Column {
                                                                Text(
                                                                    text = task.name,
                                                                    textAlign = TextAlign.Start,
                                                                    style = MaterialTheme.typography.caption
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
                                                            color = DarkGrey,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = MaterialTheme.spacing.medium,
                            bottom = 96.dp,
                            start = MaterialTheme.spacing.large,
                            end = MaterialTheme.spacing.large
                        ),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
                        onClick = {
                            // TODO: share campaign
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_share_campaign,
                                contentDescription = stringResource(R.string.participant_count),
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            Text(
                                text = stringResource(R.string.share_campaign),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.button
                            )
                        }
                    }
                }
            }
        }
    }
}