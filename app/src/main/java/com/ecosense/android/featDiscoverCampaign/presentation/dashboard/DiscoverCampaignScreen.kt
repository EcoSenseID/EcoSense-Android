package com.ecosense.android.featDiscoverCampaign.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.*
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.destinations.LoginScreenDestination
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.BrowseCategory
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.DashboardTopBar
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.OnGoingTasks
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
                        categoryName = "Search Campaign"
                    )
                )
            })
        }, scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = MaterialTheme.spacing.medium)
        ) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(MintGreen),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(
                                top = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium,
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.small
                            )
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(Orange)
                            .padding(MaterialTheme.spacing.small)
                            .weight(1f)
                    ) {
                        var countTask = 0
                        state.dashboard.tasks.forEach {
                            countTask += it.tasksLeft
                        }
                        Text(
                            text = if (state.isLoadingDashboard) stringResource(R.string.dash) else countTask.toString(),
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = stringResource(R.string.missions_to_go),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(
                                top = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium,
                                start = MaterialTheme.spacing.small,
                                end = MaterialTheme.spacing.medium
                            )
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(GradientForButtons)
                            .padding(MaterialTheme.spacing.small)
                            .weight(1f)
                    ) {
                        Text(
                            text = if (state.isLoadingDashboard) stringResource(R.string.dash) else state.dashboard.completedCampaigns.size.toString(),
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = stringResource(R.string.completed),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                OnGoingTasks(
                    navigator = navigator,
                    tasks = state.dashboard.tasks,
                    isLoading = state.isLoadingDashboard
                )
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
                categories = state.categories,
                isLoading = state.isLoadingCategories
            )
        }
    }
}