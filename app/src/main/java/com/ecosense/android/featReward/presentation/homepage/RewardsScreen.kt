package com.ecosense.android.featReward.presentation.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.CategoryRewardsScreenDestination
import com.ecosense.android.destinations.MyRewardsScreenDestination
import com.ecosense.android.destinations.RewardDetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun RewardsScreen(
    navigator: DestinationsNavigator, viewModel: RewardHomepageViewModel = hiltViewModel()
) {
    val verticalScroll = rememberScrollState()
    val donationHorizontalScroll = rememberScrollState()
    val hotDealsHorizontalScroll = rememberScrollState()

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

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = MaterialTheme.spacing.medium)
                .verticalScroll(verticalScroll)
        ) {
            Text(
                text = if (!state.user.displayName.isNullOrBlank()) state.user.displayName
                else stringResource(R.string.ecosense_user)
            )
            Text(text = state.rewardHomepage.totalPoints.toString())
            Button(
                onClick = {
                    navigator.navigate(
                        MyRewardsScreenDestination()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "My EcoRewards")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "All Rewards",
                        modifier = Modifier.clickable(onClick = {
                            navigator.navigate(
                                CategoryRewardsScreenDestination("All Rewards")
                            )
                        })
                    )
                    Text(text = "All Rewards")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Environment",
                        modifier = Modifier.clickable(onClick = {
                            navigator.navigate(
                                CategoryRewardsScreenDestination("Environment")
                            )
                        })
                    )
                    Text(text = "Environment")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Entertainment",
                        modifier = Modifier.clickable(onClick = {
                            navigator.navigate(
                                CategoryRewardsScreenDestination("Entertainment")
                            )
                        })
                    )
                    Text(text = "Entertainment")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Beverages",
                        modifier = Modifier.clickable(onClick = {
                            navigator.navigate(
                                CategoryRewardsScreenDestination("Beverages")
                            )
                        })
                    )
                    Text(text = "Beverages")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Health",
                        modifier = Modifier.clickable(onClick = {
                            navigator.navigate(
                                CategoryRewardsScreenDestination("Health")
                            )
                        })
                    )
                    Text(text = "Health")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Donation")
                Text(
                    text = "See All",
                    modifier = Modifier.clickable(onClick = {
                        navigator.navigate(
                            CategoryRewardsScreenDestination("Donation")
                        )
                    })
                )
            }
            Row(modifier = Modifier.horizontalScroll(donationHorizontalScroll)) {
                state.rewardHomepage.donationRewards.forEachIndexed { index, donation ->
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .clickable(onClick = {
                                navigator.navigate(
                                    RewardDetailScreenDestination(index)
                                )
                            })
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(donation.bannerUrl)
                                .crossfade(true)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = donation.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(text = donation.title)
                        Text(text = donation.partner)
                        Text(text = "${donation.pointsNeeded} EcoPoints")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Hot Deals")
                Text(
                    text = "See All",
                    modifier = Modifier.clickable(onClick = {
                        navigator.navigate(
                            CategoryRewardsScreenDestination("Hot Deals")
                        )
                    })
                )
            }
            Row(modifier = Modifier.horizontalScroll(hotDealsHorizontalScroll)) {
                state.rewardHomepage.hotDealsRewards.forEachIndexed { index, hotDeals ->
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .clickable(onClick = {
                                navigator.navigate(
                                    RewardDetailScreenDestination(index)
                                )
                            })
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(hotDeals.bannerUrl)
                                .crossfade(true)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = hotDeals.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(text = hotDeals.title)
                        Text(text = hotDeals.partner)
                        Text(text = "${hotDeals.pointsNeeded} EcoPoints")
                    }
                }
            }
        }
    }
}