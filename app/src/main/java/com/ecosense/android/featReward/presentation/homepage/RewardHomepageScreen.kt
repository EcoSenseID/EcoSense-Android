package com.ecosense.android.featReward.presentation.homepage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.EcoPointsColor
import com.ecosense.android.core.presentation.theme.GradientForButtons
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.OnLifecycleEvent
import com.ecosense.android.destinations.LoginScreenDestination
import com.ecosense.android.destinations.MyRewardsScreenDestination
import com.ecosense.android.destinations.RewardDetailScreenDestination
import com.ecosense.android.destinations.RewardsScreenDestination
import com.ecosense.android.featReward.data.util.ecopointsFormatter
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

    OnLifecycleEvent {
        if (it == Lifecycle.Event.ON_RESUME) {
            viewModel.getHomepage()
            viewModel.getProfile()
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

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
                    .background(brush = GradientForButtons)
            ) {
                Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
                    Text(
                        text = if (viewModel.isLoggedIn.collectAsState().value != true) stringResource(
                            R.string.login_first
                        )
                        else {
                            if (state.isLoadingRewardHomepage) stringResource(R.string.dash)
                            else {
                                if (!state.user.displayName.isNullOrBlank()) state.user.displayName
                                else stringResource(R.string.ecosense_user)
                            }
                        },
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.h5
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .padding(1.dp)
                                .border(
                                    width = 1.dp,
                                    color = EcoPointsColor,
                                    shape = CircleShape,
                                )
                                .padding(1.dp),
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_ecosense_logo_vector,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(EcoPointsColor),
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        if (viewModel.isLoggedIn.collectAsState().value == true) {
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                            Text(
                                text = if (state.isLoadingRewardHomepage) stringResource(R.string.dash) else ecopointsFormatter(
                                    state.rewardHomepage.totalPoints
                                ),
                                color = EcoPointsColor,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.body1
                            )
                        }

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                        Text(
                            text = if (viewModel.isLoggedIn.collectAsState().value != true) stringResource(
                                R.string.not_logged_ecopoints
                            )
                            else stringResource(R.string.ecopoints),
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.body1
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    if (viewModel.isLoggedIn.collectAsState().value != true) {
                        Button(
                            onClick = {
                                if (!state.isLoadingRewardHomepage) {
                                    navigator.navigate(
                                        LoginScreenDestination()
                                    )
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.background),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.login),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.button
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                if (!state.isLoadingRewardHomepage) {
                                    navigator.navigate(
                                        MyRewardsScreenDestination()
                                    )
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.background),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_price_tag,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Text(
                                text = stringResource(R.string.my_ecorewards),
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.button
                            )
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.ecoreward_tagline),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.donation),
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.h6
                )
                if (!state.isLoadingRewardHomepage) {
                    Column(
                        modifier = Modifier.clickable(onClick = {
                            navigator.navigate(
                                RewardsScreenDestination(
                                    context.resources.getString(R.string.donation),
                                    2,
                                    state.rewardHomepage.totalPoints
                                )
                            )
                        })
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.see_all),
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.subtitle1
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.height(14.dp)
                            )
                        }
                    }
                }
            }

            if (state.isLoadingRewardHomepage) {
                Row(
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.spacing.small,
                            bottom = MaterialTheme.spacing.medium,
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.medium
                        )
                        .height(205.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.wrapContentSize(), strokeWidth = 3.dp
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .horizontalScroll(donationHorizontalScroll)
                        .padding(horizontal = MaterialTheme.spacing.small)
                ) {
                    state.rewardHomepage.donationRewards.forEach { donation ->
                        Column(
                            modifier = Modifier
                                .padding(
                                    top = MaterialTheme.spacing.small,
                                    bottom = MaterialTheme.spacing.medium,
                                    start = MaterialTheme.spacing.small,
                                    end = MaterialTheme.spacing.small
                                )
                                .height(205.dp)
                                .width(180.dp)
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colors.surface)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    navigator.navigate(
                                        RewardDetailScreenDestination(
                                            donation.id, state.rewardHomepage.totalPoints
                                        )
                                    )
                                })
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(donation.bannerUrl).crossfade(true).scale(Scale.FILL)
                                        .build(),
                                    contentDescription = donation.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(
                                        top = MaterialTheme.spacing.small,
                                        start = MaterialTheme.spacing.small,
                                        end = MaterialTheme.spacing.small
                                    )
                            ) {
                                Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall)) {
                                    Text(
                                        text = donation.title,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                    Text(
                                        text = donation.partner,
                                        style = MaterialTheme.typography.caption
                                    )
                                    Row(
                                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .padding(1.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colors.primary,
                                                    shape = CircleShape,
                                                )
                                                .padding(1.dp),
                                        ) {
                                            AsyncImage(
                                                model = R.drawable.ic_ecosense_logo_vector,
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                                        Text(
                                            text = ecopointsFormatter(donation.pointsNeeded),
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.ExtraBold,
                                            style = MaterialTheme.typography.caption
                                        )

                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                                        Text(
                                            text = stringResource(R.string.ecopoints),
                                            style = MaterialTheme.typography.caption
                                        )
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
                    .padding(horizontal = MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.ewallet),
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.h6
                )
                if (!state.isLoadingRewardHomepage) {
                    Column(
                        modifier = Modifier.clickable(onClick = {
                            navigator.navigate(
                                RewardsScreenDestination(
                                    context.resources.getString(R.string.ewallet),
                                    1,
                                    state.rewardHomepage.totalPoints
                                )
                            )
                        })
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.see_all),
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.subtitle1
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.height(14.dp)
                            )
                        }
                    }
                }
            }

            if (state.isLoadingRewardHomepage) {
                Row(
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.spacing.small,
                            bottom = MaterialTheme.spacing.medium,
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.medium
                        )
                        .height(170.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.wrapContentSize(), strokeWidth = 3.dp
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .horizontalScroll(hotDealsHorizontalScroll)
                        .padding(horizontal = MaterialTheme.spacing.small)
                ) {
                    state.rewardHomepage.walletRewards.forEach { eWallet ->
                        Column(
                            modifier = Modifier
                                .padding(
                                    top = MaterialTheme.spacing.small,
                                    bottom = MaterialTheme.spacing.medium,
                                    start = MaterialTheme.spacing.small,
                                    end = MaterialTheme.spacing.small
                                )
                                .height(170.dp)
                                .width(180.dp)
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colors.surface)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    navigator.navigate(
                                        RewardDetailScreenDestination(
                                            eWallet.id, state.rewardHomepage.totalPoints
                                        )
                                    )
                                })
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(eWallet.bannerUrl).crossfade(true).scale(Scale.FILL)
                                        .build(),
                                    contentDescription = eWallet.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(
                                        top = MaterialTheme.spacing.small,
                                        start = MaterialTheme.spacing.small,
                                        end = MaterialTheme.spacing.small
                                    )
                            ) {
                                Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall)) {
                                    Text(
                                        text = eWallet.title,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                    Text(
                                        text = eWallet.partner,
                                        style = MaterialTheme.typography.caption
                                    )
                                    Row(
                                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .padding(1.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colors.primary,
                                                    shape = CircleShape,
                                                )
                                                .padding(1.dp),
                                        ) {
                                            AsyncImage(
                                                model = R.drawable.ic_ecosense_logo_vector,
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                                        Text(
                                            text = ecopointsFormatter(eWallet.pointsNeeded),
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.ExtraBold,
                                            style = MaterialTheme.typography.caption
                                        )

                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                                        Text(
                                            text = stringResource(R.string.ecopoints),
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