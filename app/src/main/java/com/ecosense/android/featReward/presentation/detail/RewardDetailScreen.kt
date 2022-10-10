package com.ecosense.android.featReward.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun RewardDetailScreen(
    navigator: DestinationsNavigator,
    rewardId: Int,
    viewModel: RewardDetailViewModel = hiltViewModel()
) {
    remember { viewModel.getRewardDetail(rewardId = rewardId) }
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val state = viewModel.state.value
    val reward = state.rewardDetail

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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
                }
            )
        },
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (!state.isLoadingRewardDetail) {
                if (reward.numberOfRedeem >= reward.maxRedeem) {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = "Redeem Limit Reached",
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        backgroundColor = Color.Gray,
                        onClick = {}
                    )
                } else {
                    if (!state.isLoadingRedeemReward) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = stringResource(
                                        R.string.redeem_reward,
                                        reward.pointsNeeded
                                    ),
                                    color = MaterialTheme.colors.onPrimary
                                )
                            },
                            backgroundColor = MaterialTheme.colors.primary,
                            onClick = {
                                viewModel.onRedeemRewardJob(rewardId = rewardId)
                            }
                        )
                    } else {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = "Redeeming Reward...",
                                    color = MaterialTheme.colors.onPrimary
                                )
                            },
                            backgroundColor = Color.Gray,
                            onClick = {}
                        )
                    }
                }
            }
        }
    ) {
        if (state.isLoadingRewardDetail) {
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
                        .height(200.dp)
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small
                        )
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                        .clip(shape = RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(reward.bannerUrl)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = stringResource(
                            R.string.poster_of_campaign,
                            reward.title
                        ),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxSize()
                    )
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
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.small),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column {
                                Row {
                                    Text(
                                        text = reward.title,
                                        style = MaterialTheme.typography.h5,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.primary,
                                        modifier = Modifier
                                            .padding(bottom = MaterialTheme.spacing.small)
                                    )
                                }
                                Row {
                                    Text(
                                        text = "Powered by ",
                                        textAlign = TextAlign.Justify,
                                        style = MaterialTheme.typography.overline,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = reward.partner,
                                        textAlign = TextAlign.Justify,
                                        style = MaterialTheme.typography.overline,
                                        fontWeight = FontWeight.Bold,
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
                                        text = "Valid until",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(bottom = MaterialTheme.spacing.small)
                                    )
                                }
                                Row {
                                    Text(
                                        text = dateFormatter(reward.validity),
                                        textAlign = TextAlign.Justify,
                                        style = MaterialTheme.typography.caption,
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
                                        text = "Description",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(bottom = MaterialTheme.spacing.small)
                                    )
                                }
                                Row {
                                    Text(
                                        text = reward.description,
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
                                        text = "Terms & Conditions",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(bottom = MaterialTheme.spacing.small)
                                    )
                                }
                                reward.termsCondition.forEachIndexed { index, term ->
                                    Row {
                                        Text(
                                            text = "${index + 1}. $term",
                                            textAlign = TextAlign.Justify,
                                            style = MaterialTheme.typography.caption
                                        )
                                    }
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
                                        text = "How to Use",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(bottom = MaterialTheme.spacing.small)
                                    )
                                }
                                reward.howToUse.forEachIndexed { index, step ->
                                    Row {
                                        Text(
                                            text = "${index + 1}. $step",
                                            textAlign = TextAlign.Justify,
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