package com.ecosense.android.featDiscoverCampaign.presentation.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.domain.constants.CampaignCompletionStatus
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.*
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.OnLifecycleEvent
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.destinations.CampaignDetailScreenDestination
import com.ecosense.android.destinations.LoginScreenDestination
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.BrowseCategory
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.DashboardTopBar
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.UncompletedMission
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
@RootNavGraph(start = true)
fun DiscoverCampaignScreen(
    navigator: DestinationsNavigator, viewModel: DiscoverCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value
    val dashboard = state.dashboard

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    OnLifecycleEvent {
        if (it == Lifecycle.Event.ON_RESUME)
            viewModel.getDashboard()
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
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            DashboardTopBar({
                navigator.navigate(
                    BrowseCampaignScreenDestination(
                        search = it,
                        categoryId = null,
                        categoryName = context.resources.getString(R.string.search_for_campaign)
                    )
                )
            })
        }, scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = MaterialTheme.spacing.medium)
                .verticalScroll(verticalScrollState)
        ) {
            if (!state.isLoadingDashboard) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_current_progress),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
                    )
                }
                if (viewModel.isLoggedIn.collectAsState().value != true) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = R.drawable.character_05,
                            contentDescription = stringResource(R.string.ecobot),
                            modifier = Modifier.height(225.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.login_to_see_missions),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    GradientButton(
                        onClick = {
                            if (!state.isLoadingDashboard) {
                                navigator.navigate(
                                    LoginScreenDestination()
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Text(
                            text = stringResource(R.string.login),
                            style = MaterialTheme.typography.body1,
                            color = White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    if (dashboard.campaigns.isEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .padding(horizontal = MaterialTheme.spacing.medium),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = R.drawable.character_04,
                                contentDescription = stringResource(R.string.ecobot),
                                modifier = Modifier.width(175.dp)
                            )
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_active_campaign),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.caption,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding()
                                )
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                                Text(
                                    text = stringResource(R.string.lets_join_one),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.caption,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else if (dashboard.campaigns.size == 1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp)
                                .padding(horizontal = MaterialTheme.spacing.medium)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clip(shape = RoundedCornerShape(20.dp))
                                .background(MintGreen)
                                .clickable {
                                    navigator.navigate(
                                        CampaignDetailScreenDestination(id = dashboard.campaigns[0].id)
                                    )
                                },
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.spacing.small),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = dashboard.campaigns[0].name,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.subtitle1,
                                        color = MaterialTheme.colors.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(
                                                bottom = MaterialTheme.spacing.medium,
                                                start = MaterialTheme.spacing.medium,
                                                end = MaterialTheme.spacing.small
                                            )
                                            .clip(shape = RoundedCornerShape(20.dp))
                                            .background(Orange)
                                            .padding(bottom = MaterialTheme.spacing.small)
                                            .weight(1f),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = dashboard.campaigns[0].missionLeft.toString(),
                                            textAlign = TextAlign.Center,
                                            fontSize = 40.sp,
                                            color = MaterialTheme.colors.onPrimary,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = stringResource(R.string.missions_to_go),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.body1,
                                            color = MaterialTheme.colors.onPrimary,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(
                                                bottom = MaterialTheme.spacing.medium,
                                                start = MaterialTheme.spacing.small,
                                                end = MaterialTheme.spacing.medium
                                            )
                                            .clip(shape = RoundedCornerShape(20.dp))
                                            .background(GradientForButtons)
                                            .padding(bottom = MaterialTheme.spacing.small)
                                            .weight(1f),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = dashboard.campaigns[0].missionCompleted.toString(),
                                            textAlign = TextAlign.Center,
                                            fontSize = 40.sp,
                                            color = MaterialTheme.colors.onPrimary,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = stringResource(R.string.completed),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.body1,
                                            color = MaterialTheme.colors.onPrimary,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }
                        UncompletedMission(
                            navigator = navigator,
                            campaigns = dashboard.campaigns[0],
                            campaignSize = dashboard.campaigns.size
                        )
                    } else {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(horizontalScrollState)) {
                            dashboard.campaigns.forEach { campaign ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (campaign.completionStatus != CampaignCompletionStatus.TIMEOUT) {
                                        Row(
                                            modifier = Modifier
                                                .width(350.dp)
                                                .height(145.dp)
                                                .padding(horizontal = MaterialTheme.spacing.medium)
                                                .shadow(
                                                    elevation = 2.dp,
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                                .clip(shape = RoundedCornerShape(20.dp))
                                                .background(MintGreen)
                                                .clickable {
                                                    navigator.navigate(
                                                        CampaignDetailScreenDestination(id = campaign.id)
                                                    )
                                                },
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(MaterialTheme.spacing.small),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = campaign.name,
                                                        textAlign = TextAlign.Center,
                                                        style = MaterialTheme.typography.subtitle1,
                                                        color = MaterialTheme.colors.primary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(
                                                                bottom = MaterialTheme.spacing.medium,
                                                                start = MaterialTheme.spacing.medium,
                                                                end = MaterialTheme.spacing.small
                                                            )
                                                            .clip(shape = RoundedCornerShape(20.dp))
                                                            .background(Orange)
                                                            .padding(bottom = MaterialTheme.spacing.small)
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Text(
                                                            text = campaign.missionLeft.toString(),
                                                            textAlign = TextAlign.Center,
                                                            fontSize = 40.sp,
                                                            color = MaterialTheme.colors.onPrimary,
                                                            fontWeight = FontWeight.ExtraBold
                                                        )
                                                        Text(
                                                            text = stringResource(R.string.missions_to_go),
                                                            textAlign = TextAlign.Center,
                                                            style = MaterialTheme.typography.body1,
                                                            color = MaterialTheme.colors.onPrimary,
                                                            fontWeight = FontWeight.ExtraBold
                                                        )
                                                    }
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(
                                                                bottom = MaterialTheme.spacing.medium,
                                                                start = MaterialTheme.spacing.small,
                                                                end = MaterialTheme.spacing.medium
                                                            )
                                                            .clip(shape = RoundedCornerShape(20.dp))
                                                            .background(GradientForButtons)
                                                            .padding(bottom = MaterialTheme.spacing.small)
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Text(
                                                            text = campaign.missionCompleted.toString(),
                                                            textAlign = TextAlign.Center,
                                                            fontSize = 40.sp,
                                                            color = MaterialTheme.colors.onPrimary,
                                                            fontWeight = FontWeight.ExtraBold
                                                        )
                                                        Text(
                                                            text = stringResource(R.string.completed),
                                                            textAlign = TextAlign.Center,
                                                            style = MaterialTheme.typography.body1,
                                                            color = MaterialTheme.colors.onPrimary,
                                                            fontWeight = FontWeight.ExtraBold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        UncompletedMission(
                                            navigator = navigator,
                                            campaigns = campaign,
                                            campaignSize = dashboard.campaigns.size
                                        )
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
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = stringResource(R.string.browse_categories),
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colors.onSurface,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
                        )
                    }
                }
                BrowseCategory(
                    navigator = navigator,
                    categories = dashboard.categories,
                    isLoading = state.isLoadingCategories
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.wrapContentSize(),
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
}