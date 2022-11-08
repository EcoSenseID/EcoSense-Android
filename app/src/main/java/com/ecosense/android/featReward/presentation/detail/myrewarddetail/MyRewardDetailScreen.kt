package com.ecosense.android.featReward.presentation.detail.myrewarddetail

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.theme.CustardYellow
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ecosense.android.featReward.presentation.detail.component.RewardItemDetail
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun MyRewardDetailScreen(
    navigator: DestinationsNavigator,
    claimId: Int,
    viewModel: MyRewardDetailViewModel = hiltViewModel()
) {
    remember { viewModel.getMyRewardDetail(claimId = claimId) }
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val state = viewModel.state.value
    val myReward = state.myRewardDetail

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
            if (!state.isLoadingMyRewardDetail) {
                when (myReward.claimStatus) {
                    1 -> {
                        if (!state.isLoadingUseReward) {
                            ExtendedFloatingActionButton(
                                text = {
                                    Text(
                                        text = "Use Reward",
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                },
                                backgroundColor = MaterialTheme.colors.primary,
                                onClick = {
                                    viewModel.onUseRewardJob(claimId = claimId)
                                }
                            )
                        } else {
                            ExtendedFloatingActionButton(
                                text = {
                                    Text(
                                        text = "Using Reward...",
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                },
                                backgroundColor = Color.Gray,
                                onClick = {}
                            )
                        }
                    }
                    2 -> {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = "Requested",
                                    color = MaterialTheme.colors.onPrimary
                                )
                            },
                            backgroundColor = CustardYellow,
                            onClick = {}
                        )
                    }
                    3 -> {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = "Completed",
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
        if (state.isLoadingMyRewardDetail) {
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
                reward = null,
                myReward = myReward,
                scrollState = scrollState,
                context = context
            )
        }
    }
}