package com.ecosense.android.featReward.presentation.categoryrewards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.RewardDetailScreenDestination
import com.ecosense.android.featReward.presentation.component.RewardItem
import com.ecosense.android.featReward.presentation.component.RewardTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun CategoryRewardsScreen(
    navigator: DestinationsNavigator,
    rewardCategory: String,
    viewModel: CategoryRewardsViewModel = hiltViewModel()
) {
    remember { viewModel.getRewards(rewardCategory = rewardCategory) }

    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value

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
            RewardTopBar(
                onBackClick = {
                    navigator.popBackStack()
                },
                screenName = rewardCategory
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                if (state.isLoadingRewardList) {
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
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.rewards.size) { i ->
                        RewardItem(
                            reward = state.rewards[i],
                            myReward = null,
                            onClick = {
                                navigator.navigate(RewardDetailScreenDestination(rewardId = state.rewards[i].id))
                            }
                        )
                    }
                }
            }
        }
    }
}