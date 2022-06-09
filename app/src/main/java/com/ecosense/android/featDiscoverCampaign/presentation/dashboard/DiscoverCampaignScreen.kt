package com.ecosense.android.featDiscoverCampaign.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.destinations.CategoryCampaignScreenDestination
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
    navigator: DestinationsNavigator,
    viewModel: DiscoverCampaignViewModel = hiltViewModel()
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
            }
        }
    }

    Scaffold(
        topBar = { DashboardTopBar({ navigator.navigate(BrowseCampaignScreenDestination(search = it, categoryId = null)) }) },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(end = MaterialTheme.spacing.small)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.primary)
                        .padding(
                            horizontal = MaterialTheme.spacing.large,
                            vertical = MaterialTheme.spacing.medium
                        )
                        .weight(1f)
                ) {
                    var countTask = 0
                    state.dashboard.tasks.forEach {
                        countTask += it.tasksLeft
                    }
                    Text(
                        text = if (state.isLoadingDashboard) stringResource(R.string.dash) else countTask.toString(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text = stringResource(R.string.tasks_to_go),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = MaterialTheme.spacing.small)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.secondary)
                        .padding(
                            horizontal = MaterialTheme.spacing.large,
                            vertical = MaterialTheme.spacing.medium
                        )
                        .weight(1f)
                ) {
                    Text(
                        text = if (state.isLoadingDashboard) stringResource(R.string.dash) else state.dashboard.completedCampaigns.size.toString(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSecondary
                    )
                    Text(
                        text = stringResource(R.string.completed),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
            Text(
                text = stringResource(R.string.on_going_task),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
            OnGoingTasks(
                navigator = navigator,
                tasks = state.dashboard.tasks,
                isLoading = state.isLoadingDashboard
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MaterialTheme.spacing.large,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(R.string.browse_categories),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
                    )
                }
                if (!state.isLoadingCategories) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = AnnotatedString(stringResource(R.string.view_all)),
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier.clickable {
                                navigator.navigate(CategoryCampaignScreenDestination)
                            }
                        )
                    }
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