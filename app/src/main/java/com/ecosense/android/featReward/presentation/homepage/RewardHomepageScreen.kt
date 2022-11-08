package com.ecosense.android.featReward.presentation.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ecosense.android.destinations.MyRewardsScreenDestination
import com.ecosense.android.destinations.RewardDetailScreenDestination
import com.ecosense.android.destinations.RewardsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun RewardHomepageScreen(
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
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "My EcoRewards")
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
                            RewardsScreenDestination(
                                "Donation",
                                2,
                                state.rewardHomepage.totalPoints
                            )
                        )
                    })
                )
            }
            Row(modifier = Modifier.horizontalScroll(donationHorizontalScroll)) {
                state.rewardHomepage.donationRewards.forEach { donation ->
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .clickable(onClick = {
                                navigator.navigate(
                                    RewardDetailScreenDestination(
                                        donation.id,
                                        state.rewardHomepage.totalPoints
                                    )
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
                Text(text = "E-Wallet")
                Text(
                    text = "See All",
                    modifier = Modifier.clickable(onClick = {
                        navigator.navigate(
                            RewardsScreenDestination(
                                "E-Wallet",
                                1,
                                state.rewardHomepage.totalPoints
                            )
                        )
                    })
                )
            }
            Row(modifier = Modifier.horizontalScroll(hotDealsHorizontalScroll)) {
                state.rewardHomepage.walletRewards.forEach { eWallet ->
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .clickable(onClick = {
                                navigator.navigate(
                                    RewardDetailScreenDestination(
                                        eWallet.id,
                                        state.rewardHomepage.totalPoints
                                    )
                                )
                            })
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(eWallet.bannerUrl)
                                .crossfade(true)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = eWallet.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(text = eWallet.title)
                        Text(text = eWallet.partner)
                        Text(text = "${eWallet.pointsNeeded} EcoPoints")
                    }
                }
            }
        }
    }
}