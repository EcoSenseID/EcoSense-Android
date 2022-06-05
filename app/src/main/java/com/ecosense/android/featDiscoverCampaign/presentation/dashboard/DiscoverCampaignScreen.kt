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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.Gray800
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.destinations.CategoryCampaignScreenDestination
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.BrowseCategory
//import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.BrowseCategory
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.DashboardTopBar
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component.OnGoingTasks
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@RootNavGraph(start = true)
fun DiscoverCampaignScreen(
    navigator: DestinationsNavigator,
    viewModel: DiscoverCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value

    Scaffold(
        topBar = { DashboardTopBar({ navigator.navigate(BrowseCampaignScreenDestination(search = it, categoryId = null)) }) },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        text = countTask.toString(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text = stringResource(R.string.tasks_left),
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
                        text = state.dashboard.tasks.size.toString(),
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
                color = Gray800,
                modifier = Modifier.padding(top = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.small)
            )
            OnGoingTasks(navigator = navigator, tasks = state.dashboard.tasks)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(R.string.browse_categories),
                        fontWeight = FontWeight.SemiBold,
                        color = Gray800,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
                    )
                }
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
            BrowseCategory(navigator = navigator, categories = state.categories)
        }
    }
}