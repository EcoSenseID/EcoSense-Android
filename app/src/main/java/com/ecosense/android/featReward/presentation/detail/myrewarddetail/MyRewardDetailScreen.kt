package com.ecosense.android.featReward.presentation.detail.myrewarddetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.*
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
                            GradientButton(
                                onClick = {
                                    viewModel.onUseRewardJob(claimId = claimId)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                            ) {
                                Text(
                                    text = stringResource(R.string.use_reward),
                                    style = MaterialTheme.typography.body1,
                                    color = White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            ExtendedFloatingActionButton(
                                text = {
                                    Text(
                                        text = stringResource(R.string.using_reward),
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
                    2 -> {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = stringResource(R.string.requested),
                                    style = MaterialTheme.typography.body1,
                                    color = White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            backgroundColor = CustardYellow,
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                        )
                    }
                    3 -> {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = stringResource(R.string.completed),
                                    style = MaterialTheme.typography.body1,
                                    color = White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            backgroundColor = SuperDarkGrey,
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
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