package com.ecosense.android.featReward.presentation.list.myrewards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.*
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.MyRewardDetailScreenDestination
import com.ecosense.android.featReward.presentation.component.RewardTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun MyRewardsScreen(
    navigator: DestinationsNavigator,
    viewModel: MyRewardsViewModel = hiltViewModel()
) {
    remember { viewModel.getMyRewards() }

    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value
    val myReward = state.myRewards

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
                screenName = "My EcoRewards"
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                if (state.isLoadingMyRewardList) {
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
                    items(state.myRewards.size) { i ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    horizontal = MaterialTheme.spacing.medium,
                                    vertical = MaterialTheme.spacing.small
                                )
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clickable(onClick = {
                                    navigator.navigate(
                                        MyRewardDetailScreenDestination(claimId = myReward[i].claimId)
                                    )
                                })
                                .background(MaterialTheme.colors.surface)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(160.dp)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(myReward[i].bannerUrl)
                                        .crossfade(true)
                                        .scale(Scale.FILL)
                                        .build(),
                                    contentDescription = myReward[i].title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                            ) {
                                Text(
                                    text = myReward[i].title,
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = myReward[i].partner,
                                    style = MaterialTheme.typography.subtitle2
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                                when (myReward[i].claimStatus) {
                                    1 -> {
                                        if (!state.isLoadingUseReward) {
                                            GradientButton(
                                                onClick = {
                                                    viewModel.onUseRewardJob(claimId = myReward[i].claimId)
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(30.dp),
                                                shape = RoundedCornerShape(20.dp)
                                            ) {
                                                Text(
                                                    text = "Use Now",
                                                    style = MaterialTheme.typography.overline,
                                                    color = White,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                                                )
                                            }
                                        } else {
                                            Button(
                                                onClick = {},
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(30.dp),
                                                shape = RoundedCornerShape(20.dp),
                                                colors = ButtonDefaults.buttonColors(DarkGrey)
                                            ) {
                                                Text(
                                                    text = "Using Reward...",
                                                    style = MaterialTheme.typography.overline,
                                                    color = White,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                                                )
                                            }
                                        }
                                    }
                                    2 -> {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            colors = ButtonDefaults.buttonColors(CustardYellow)
                                        ) {
                                            Text(
                                                text = "Requested",
                                                style = MaterialTheme.typography.overline,
                                                color = White,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                                            )
                                        }
                                    }
                                    3 -> {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            colors = ButtonDefaults.buttonColors(SuperDarkGrey)
                                        ) {
                                            Text(
                                                text = "Completed",
                                                style = MaterialTheme.typography.overline,
                                                color = White,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
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