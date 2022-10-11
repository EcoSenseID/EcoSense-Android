package com.ecosense.android.featReward.presentation.detail.rewarddetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ecosense.android.featReward.presentation.detail.component.RewardItemDetail
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
            RewardItemDetail(
                reward = reward,
                myReward = null,
                scrollState = scrollState,
                context = context
            )
        }
    }
}